package co.com.bancolombia.config;

import co.com.bancolombia.SenderQueueAdapter;
import co.com.bancolombia.cognito.signin.gateways.SignInRepository;
import co.com.bancolombia.cognito.signup.gateways.SignUpRepository;
import co.com.bancolombia.cognito.signup.usecase.management.CryptoManagementUseCase;
import co.com.bancolombia.cognito.signup.usecase.movement.CryptoMovementUseCase;
import co.com.bancolombia.cognito.signup.usecase.passwordbusinessvalidator.PasswordBusinessValidator;
import co.com.bancolombia.cognito.signup.usecase.passwordbusinessvalidator.PasswordBusinessValidatorUseCase;
import co.com.bancolombia.cognito.signup.usecase.savebackup.SaveBackupUseCase;
import co.com.bancolombia.cognito.signup.usecase.suid.SuidUseCase;
import co.com.bancolombia.common.util.HashPassToCognitoAdapter;
import co.com.bancolombia.kms.gateways.AsymmetricKmsAdapter;
import co.com.bancolombia.management.gateway.CryptoManagementRepository;
import co.com.bancolombia.movement.gateway.CryptoMovementRepository;
import co.com.bancolombia.parameters.gateways.ParameterRepository;
import co.com.bancolombia.rabbitsender.gateways.RabbitSenderRepository;
import co.com.bancolombia.savebackup.SaveBackupRepository;
import co.com.bancolombia.sqs.gateway.ISqsRepository;
import co.com.bancolombia.suid.gateways.SuidRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class UseCasesConfigTest {

    @Test
    void signUpRepository() {
        SignUpRepository signUpRepository = Mockito.mock(SignUpRepository.class);
        AsymmetricKmsAdapter asymmetricKmsAdapter = Mockito.mock(AsymmetricKmsAdapter.class);
        HashPassToCognitoAdapter hashPassToCognitoAdapter = Mockito.mock(HashPassToCognitoAdapter.class);
        SaveBackupUseCase saveBackupUseCase = Mockito.mock(SaveBackupUseCase.class);
        PasswordBusinessValidator passwordBusinessValidator = Mockito.mock(PasswordBusinessValidator.class);
        SuidUseCase suidUseCase = Mockito.mock(SuidUseCase.class);
        SenderQueueAdapter senderQueueAdapter = Mockito.mock(SenderQueueAdapter.class);
        PasswordBusinessValidatorUseCase passwordBusinessValidatorUseCase = Mockito.mock(PasswordBusinessValidatorUseCase.class);
        CryptoManagementUseCase cryptoManagementUseCase = Mockito.mock(CryptoManagementUseCase.class);
        RabbitSenderRepository rabbitSenderRepository = Mockito.mock(RabbitSenderRepository.class);

        ISqsRepository iSqsRepository = Mockito.mock(ISqsRepository.class);
        UseCasesConfig useCasesConfig = new UseCasesConfig();
        useCasesConfig
                .signUpRepository(signUpRepository,
                        asymmetricKmsAdapter,
                        rabbitSenderRepository,
                        hashPassToCognitoAdapter,
                        saveBackupUseCase,
                        passwordBusinessValidator,
                        suidUseCase,
                        senderQueueAdapter,
                        passwordBusinessValidatorUseCase,
                        cryptoManagementUseCase,
                        iSqsRepository);
        assertNotNull(useCasesConfig);
    }

    @Test
    void getSaveBackupUseCase() {
        SaveBackupRepository saveBackupRepository = Mockito.mock(SaveBackupRepository.class);
        UseCasesConfig useCasesConfig = new UseCasesConfig();
        useCasesConfig
                .getSaveBackupUseCase(saveBackupRepository);
        assertNotNull(useCasesConfig);
    }

    @Test
    void getPasswordBusinessValidator() {
        UseCasesConfig useCasesConfig = new UseCasesConfig();
        useCasesConfig
                .getPasswordBusinessValidator();
        assertNotNull(useCasesConfig);
    }

    @Test
    void getSuidUseCase() {
        SuidRepository suidRepository = Mockito.mock(SuidRepository.class);
        UseCasesConfig useCasesConfig = new UseCasesConfig();
        useCasesConfig
                .getSuidUseCase(suidRepository);
        assertNotNull(useCasesConfig);
    }

    @Test
    void getPasswordValidationUseCase() {
        ParameterRepository parameterRepository = Mockito.mock(ParameterRepository.class);
        PasswordBusinessValidator passwordBusinessValidator = Mockito.mock(PasswordBusinessValidator.class);
        UseCasesConfig useCasesConfig = new UseCasesConfig();
        useCasesConfig
                .getPasswordValidationUseCase(parameterRepository,passwordBusinessValidator);
        assertNotNull(useCasesConfig);
    }

    @Test
    void getCryptoManagementUseCase() {
        CryptoManagementRepository cryptoManagementRepository = Mockito.mock(CryptoManagementRepository.class);
        CryptoMovementUseCase cryptoMovementUseCase = Mockito.mock(CryptoMovementUseCase.class);
        UseCasesConfig useCasesConfig = new UseCasesConfig();
        useCasesConfig
                .getCryptoManagementUseCase(cryptoManagementRepository,cryptoMovementUseCase);
        assertNotNull(useCasesConfig);
    }

    @Test
    void getCryptoMovementUseCase() {
        CryptoMovementRepository cryptoMovementRepository = Mockito.mock(CryptoMovementRepository.class);
        ParameterRepository parameterRepository = Mockito.mock(ParameterRepository.class);
        UseCasesConfig useCasesConfig = new UseCasesConfig();
        useCasesConfig
                .getCryptoMovementUseCase(cryptoMovementRepository,parameterRepository);
        assertNotNull(useCasesConfig);
    }

    @Test
    void resources() {
        UseCasesConfig useCasesConfig = new UseCasesConfig();
        useCasesConfig
                .resources();
        assertNotNull(useCasesConfig);
    }
}