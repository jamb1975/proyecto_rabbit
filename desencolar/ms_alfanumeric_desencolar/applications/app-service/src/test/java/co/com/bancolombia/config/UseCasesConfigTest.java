package co.com.bancolombia.config;

import co.com.bancolombia.cognito.signup.gateways.SignUpRepository;
import co.com.bancolombia.userregister.gateways.UserRegisterRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UseCasesConfigTest {

    @Test
    void useRegisterUseCase() {
        UserRegisterRepository userRegisterRepository = Mockito.mock(UserRegisterRepository.class);
        UseCasesConfig useCasesConfig = new UseCasesConfig();
        useCasesConfig
                .useRegisterUseCase(userRegisterRepository);
        assertNotNull(useCasesConfig);
    }

    
    @Test
    void asyncLogsCloudWatchUseCase() {
        UseCasesConfig useCasesConfig = new UseCasesConfig();
        useCasesConfig
                .asyncLogsCloudWatchUseCase();
        assertNotNull(useCasesConfig);
    }
}