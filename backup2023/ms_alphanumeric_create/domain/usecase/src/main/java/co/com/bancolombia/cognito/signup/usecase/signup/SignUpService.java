package co.com.bancolombia.cognito.signup.usecase.signup;

import co.com.bancolombia.SenderQueueAdapter;
import co.com.bancolombia.cognito.admin.OpsForUser;
import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.cognito.signup.usecase.savebackup.SaveBackupUseCase;
import co.com.bancolombia.cognito.signup.usecase.management.CryptoManagementUseCase;
import co.com.bancolombia.cognito.signup.usecase.signup.gateway.SignUpUseCase;
import co.com.bancolombia.cognito.signup.gateways.SignUpRepository;
import co.com.bancolombia.cognito.signup.usecase.passwordbusinessvalidator.PasswordBusinessValidator;
import co.com.bancolombia.cognito.signup.usecase.passwordbusinessvalidator.PasswordBusinessValidatorUseCase;
import co.com.bancolombia.cognito.signup.usecase.suid.SuidUseCase;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.common.util.HashPassToCognitoAdapter;
import co.com.bancolombia.kms.gateways.AsymmetricKmsAdapter;
import co.com.bancolombia.management.CryptoManagementEntity;
import co.com.bancolombia.passwordvalidatormodel.PasswordValidatorModel;
import co.com.bancolombia.sqs.gateway.ISqsRepository;
import co.com.bancolombia.suid.response.AttributesGetAlias;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;
import co.com.bancolombia.rabbitsender.gateways.RabbitSenderRepository;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
@Log4j2
public class SignUpService implements SignUpUseCase {

    private final SignUpRepository signUpRepository;
    private final AsymmetricKmsAdapter asymmetricKmsAdapter;
    private final HashPassToCognitoAdapter hashPassToCognitoAdapter;
    private final SenderQueueAdapter senderQueueAdapter;
    private final SaveBackupUseCase saveBackupUseCase;
    private final SuidUseCase suidUseCase;
    private final PasswordBusinessValidator passwordBusinessValidator;
    private final PasswordBusinessValidatorUseCase passwordBusinessValidatorUseCase;
    private final CryptoManagementUseCase cryptoManagementUseCase;
    private final  RabbitSenderRepository rabbitSenderRepository;
    private final ISqsRepository iSqsRepository;


    public SignUpService(SignUpRepository signUpRepository,
                         AsymmetricKmsAdapter asymmetricKmsAdapter,
                         HashPassToCognitoAdapter hashPassToCognitoAdapter,
                         SaveBackupUseCase saveBackupUseCase,
                         RabbitSenderRepository rabbitSenderRepository,
                         PasswordBusinessValidator passwordBusinessValidator,
                         SuidUseCase suidUseCase, SenderQueueAdapter senderQueueAdapter,
                         PasswordBusinessValidatorUseCase passwordBusinessValidatorUseCase,
                         CryptoManagementUseCase cryptoManagementUseCase,
                         ISqsRepository iSqsRepository) {
        this.senderQueueAdapter = senderQueueAdapter;
        this.signUpRepository = signUpRepository;
        this.asymmetricKmsAdapter = asymmetricKmsAdapter;
        this.hashPassToCognitoAdapter = hashPassToCognitoAdapter;
        this.saveBackupUseCase = saveBackupUseCase;
        this.passwordBusinessValidator = passwordBusinessValidator;
        this.suidUseCase = suidUseCase;
        this.passwordBusinessValidatorUseCase = passwordBusinessValidatorUseCase;
        this.cryptoManagementUseCase = cryptoManagementUseCase;
        this.rabbitSenderRepository = rabbitSenderRepository;
        this.iSqsRepository = iSqsRepository;
    }

    @Override
    public Mono<ResponseEntity<ApiResponse>> startSignUpFlow(final PasswordManagementRequest passwordManagementRequest,
                                                             final String messageId,
                                                             final String consumer) {
        var iniCreate = System.currentTimeMillis();
        var aid = passwordManagementRequest.getAid();

        if (!passwordBusinessValidator.isAlphanumeric(messageId)) {
            return buildBusinessException(null, messageId, consumer, Constants.INVALID_IDENTIFIER);
        }
        if (passwordBusinessValidator.requiredFieldIsNullOrEmpty(passwordManagementRequest,
                messageId, consumer) != null) {
            return buildBusinessException(null, messageId, consumer, Constants.MISSING_BODY_CODE);
        }
        var voWithDecrypted = decryptPassWord(passwordManagementRequest);

        if (voWithDecrypted == null || !validatePasswordTimeStamp(voWithDecrypted)) {
            return buildBusinessException(aid, messageId, consumer,
                    Constants.PASSWORD_DOES_NOT_MEET_ACCEPTANCE_CRITERIA);
        }

        var attributesGetAlias = validationSUID(passwordManagementRequest.getAid(), consumer);

        if (attributesGetAlias == null) {
            return buildBusinessException(aid, messageId, consumer,
                    Constants.USER_NOT_AVAILABLE);
        }

        if (!passwordBusinessValidations(consumer, voWithDecrypted)) {
            return buildBusinessException(aid, messageId, consumer,
                    Constants.PASSWORD_DOES_NOT_MEET_ACCEPTANCE_CRITERIA);
        }

        var voWithHashPass = hashPassWord(voWithDecrypted);

        var requestApiResponse = sendMenssageRabbitMq(voWithHashPass.toBuilder().consumer(consumer).messageId(messageId).build());


        if (requestApiResponse.getData() != null) {
            saveUser(consumer, voWithHashPass);
            saveBackup(voWithHashPass, messageId, consumer);
            //Proceso Async Sqs
            log.info("Sending Sqs");
            iSqsRepository.sendOneMsgToQueue(Constants.GSON.toJson(voWithHashPass));

        }
        var finCreate = System.currentTimeMillis();
        System.out.println("Tiempo de Total Creacion: " + (finCreate - iniCreate) / 1000F);
        System.out.println(
                "-------------------------------------------------------------------------------");
        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(requestApiResponse));
    }

    public ApiResponse sendMenssageRabbitMq(final PasswordManagementRequest passwordManagementRequest)  {
        return Mono.just(passwordManagementRequest)
                .flatMap(voWithHashPass -> {
                    try {
                        return rabbitSenderRepository.consumer((PasswordManagementRequest) voWithHashPass);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (TimeoutException e) {
                        throw new RuntimeException(e);
                    }
                }).block();
    }

    private Mono<ResponseEntity<ApiResponse>> buildBusinessException(String aid, String messageId, String consumerAcronym,
                                                                     String codError) {
        return Mono.just(ResponseEntity.badRequest().body(ApiResponse.createOnError(messageId, consumerAcronym, codError)));
    }

    @Override
    public String deleteUser(OpsForUser opsForUser) {
        return signUpRepository.deleteUser(opsForUser);
    }

    public PasswordManagementRequest decryptPassWord(
            final PasswordManagementRequest passwordManagementRequest) {
        var iniDecrypt = System.currentTimeMillis();
        String decryptedPass = asymmetricKmsAdapter.decryptWithKms(
                passwordManagementRequest.getPassword());
        var rawPassword = decryptedPass.substring(0, decryptedPass.lastIndexOf("|"));
        var timeStamp = decryptedPass.substring(decryptedPass.lastIndexOf("|") + Constants.ONE);
        var finDecrypt = System.currentTimeMillis();
        System.out.println("Tiempo de DecryptPassWord: " + (finDecrypt - iniDecrypt) / 1000F);
        return passwordManagementRequest.toBuilder().password(rawPassword).timeStamp(timeStamp).build();
    }

    public PasswordManagementRequest hashPassWord(
            PasswordManagementRequest passwordManagementRequest) {
        return hashPassToCognitoAdapter.hashPass(passwordManagementRequest);
    }

    public void sendQueue(
            PasswordManagementRequest passwordManagementRequest) {
        senderQueueAdapter.sendQueue(passwordManagementRequest);
    }

    public void saveBackup(PasswordManagementRequest passwordManagementRequest, String messageId, String consumer) {
        saveBackupUseCase.saveBackup(passwordManagementRequest, messageId, consumer);
    }

    public boolean saveUser(final String consumer,
                         final PasswordManagementRequest passwordManagementRequest) {
        var saveUserRequest = CryptoManagementEntity.builder()
                .aid(passwordManagementRequest.getAid())
                .consumer(consumer).build();
        return cryptoManagementUseCase.createCryptoManagement(saveUserRequest);
    }

    private void saveErrorLog(String aid, String messageId) {
        /*
        var logRequest = LogRequest.builder()
                .classification(USER).
                typeAction(CREATE).
                actor(aid).
                beforeValue(BEFORE_VALUE).
                afterValue(BEFORE_VALUE).
                resultCode(Constants.PASSWORD_DOES_NOT_MEET_ACCEPTANCE_CRITERIA).
                resultDescription(RESULT_DESCRIPTION_ERROR).
                app(APP).
                messageId(messageId).
                additionalInfo("{}").
                description(DESCRIPTION).
                build();
        logsUseCase.saveLog(logRequest);
        */
    }

    private AttributesGetAlias validationSUID(String aid, String consumer) {
        return suidUseCase.getAliasByAidConsumer(aid, consumer);
    }

    private boolean validatePasswordTimeStamp(
            final PasswordManagementRequest passwordManagementRequest) {
        return passwordBusinessValidatorUseCase.validatePasswordTimeStamp(
                passwordManagementRequest.getTimeStamp());

    }

    private boolean passwordBusinessValidations(final String consumer,
                                                final PasswordManagementRequest passwordManagementRequest) {
        return passwordBusinessValidatorUseCase.validatePassword(
                PasswordValidatorModel.builder().consumer(consumer)
                        .password(passwordManagementRequest.getPassword()).build());

    }

}
