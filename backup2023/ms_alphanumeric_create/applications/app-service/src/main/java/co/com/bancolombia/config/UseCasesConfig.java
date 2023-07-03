package co.com.bancolombia.config;

import co.com.bancolombia.SenderQueueAdapter;
import co.com.bancolombia.cognito.signup.usecase.management.CryptoManagementUseCase;
import co.com.bancolombia.cognito.signup.usecase.movement.CryptoMovementUseCase;
import co.com.bancolombia.cognito.signup.usecase.signup.gateway.SignUpUseCase;
import co.com.bancolombia.cognito.signup.gateways.SignUpRepository;
import co.com.bancolombia.cognito.signup.usecase.savebackup.SaveBackupUseCase;
import co.com.bancolombia.cognito.signup.usecase.signup.SignUpService;
import co.com.bancolombia.cognito.signup.usecase.passwordbusinessvalidator.PasswordBusinessValidator;
import co.com.bancolombia.cognito.signup.usecase.passwordbusinessvalidator.PasswordBusinessValidatorUseCase;
import co.com.bancolombia.cognito.signup.usecase.suid.SuidUseCase;
import co.com.bancolombia.common.util.HashPassToCognitoAdapter;
import co.com.bancolombia.kms.gateways.AsymmetricKmsAdapter;
import co.com.bancolombia.management.gateway.CryptoManagementRepository;
import co.com.bancolombia.movement.gateway.CryptoMovementRepository;
import co.com.bancolombia.parameters.gateways.ParameterRepository;
import co.com.bancolombia.rabbitsender.gateways.RabbitSenderRepository;
import co.com.bancolombia.savebackup.SaveBackupRepository;
import co.com.bancolombia.secret.ServicesCredentials;
import co.com.bancolombia.sqs.gateway.ISqsRepository;
import co.com.bancolombia.suid.gateways.SuidRepository;
import co.com.bancolombia.util.AwsUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public ServicesCredentials servicesCredentials(
        @Value("${aws.credentialsSecretName}") String secretName) {
        return AwsUtils.getSecret(secretName, ServicesCredentials.class);
    }

    @Bean
    public SignUpUseCase signUpRepository(SignUpRepository signUpRepository,
                                          AsymmetricKmsAdapter asymmetricKmsAdapter,
                                          RabbitSenderRepository rabbitSenderRepository,
                                          HashPassToCognitoAdapter hashPassToCognitoAdapter,
                                          SaveBackupUseCase saveBackupUseCase,
                                          PasswordBusinessValidator passwordBusinessValidator,
                                          SuidUseCase suidUseCase, SenderQueueAdapter senderQueueAdapter,
                                          PasswordBusinessValidatorUseCase passwordBusinessValidatorUseCase,
                                          CryptoManagementUseCase cryptoManagementUseCase,
                                          ISqsRepository iSqsRepository) {
        return new SignUpService(signUpRepository,
                   asymmetricKmsAdapter,
                   hashPassToCognitoAdapter,
                   saveBackupUseCase,
                   rabbitSenderRepository,
                   passwordBusinessValidator,
                   suidUseCase,
                   senderQueueAdapter,
                   passwordBusinessValidatorUseCase,
                   cryptoManagementUseCase,
                   iSqsRepository);
    }

    @Bean
    public SaveBackupUseCase getSaveBackupUseCase(SaveBackupRepository saveBackupRepository) {
        return new SaveBackupUseCase(saveBackupRepository);
    }

    @Bean
    public PasswordBusinessValidator getPasswordBusinessValidator() {
        return new PasswordBusinessValidator();
    }

    @Bean
    public SuidUseCase getSuidUseCase(final SuidRepository suidRepository) {
        return new SuidUseCase(suidRepository);
    }

    @Bean
    public PasswordBusinessValidatorUseCase getPasswordValidationUseCase(
            final ParameterRepository parameterRepository,
            final PasswordBusinessValidator passwordBusinessValidator) {
        return new PasswordBusinessValidatorUseCase(parameterRepository, passwordBusinessValidator);
    }

    @Bean
    public CryptoManagementUseCase getCryptoManagementUseCase(
            final CryptoManagementRepository managementRepository,
            final CryptoMovementUseCase cryptoMovementUseCase) {
        return new CryptoManagementUseCase(managementRepository, cryptoMovementUseCase);
    }

    @Bean
    public CryptoMovementUseCase getCryptoMovementUseCase(
            CryptoMovementRepository movementRepository, ParameterRepository paramsRepository) {
        return new CryptoMovementUseCase(movementRepository, paramsRepository);
    }

    @Bean
    public WebProperties.Resources resources(){
        return new WebProperties.Resources();
    }

}
