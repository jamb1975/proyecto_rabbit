package co.com.bancolombia.config;

import co.com.bancolombia.cognito.signup.gateways.SignUpRepository;
import co.com.bancolombia.logscloudwatch.AsyncLogsCloudWatchUseCase;
import co.com.bancolombia.useregister.UseRegisterUseCase;
import co.com.bancolombia.userregister.gateways.UserRegisterRepository;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {
        @Bean
        public UseRegisterUseCase useRegisterUseCase(UserRegisterRepository userRegisterRepository){
                return new UseRegisterUseCase(userRegisterRepository);
        }

        @Bean
        public AsyncLogsCloudWatchUseCase asyncLogsCloudWatchUseCase(){
                return new AsyncLogsCloudWatchUseCase();
        }
}

