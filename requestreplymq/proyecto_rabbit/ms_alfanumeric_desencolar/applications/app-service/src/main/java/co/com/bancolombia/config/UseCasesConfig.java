package co.com.bancolombia.config;

import co.com.bancolombia.secret.ServicesCredentials;
import co.com.bancolombia.useregister.UseRegisterUseCase;
import co.com.bancolombia.userregister.gateways.UserRegisterRepository;
import co.com.bancolombia.utill.AwsUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "co.com.bancolombia.kms.gateways")
        public class UseCasesConfig {

        @Bean
        @ConditionalOnMissingBean
        public UseRegisterUseCase useRegisterUseCase(UserRegisterRepository userRegisterRepository){
                return new UseRegisterUseCase(userRegisterRepository);
        }

        @Bean
        public ServicesCredentials servicesCredentials(
                @Value("${aws.credentialsSecretName}") String secretName) {
                return AwsUtils.getSecret(secretName, ServicesCredentials.class);
        }




}

