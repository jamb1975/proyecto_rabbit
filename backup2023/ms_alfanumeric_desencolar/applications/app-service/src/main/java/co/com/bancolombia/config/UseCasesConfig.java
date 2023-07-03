package co.com.bancolombia.config;

import co.com.bancolombia.cognito.signin.gateways.SignInRepository;
import co.com.bancolombia.cognito.signup.gateways.SignUpRepository;
import co.com.bancolombia.cognito.update.gateways.UpdateRepository;
import co.com.bancolombia.rabbitmqadapter.RabbitMqAdapter;
import co.com.bancolombia.rabbitmqadapter.RabbitToCognito;
import co.com.bancolombia.rabbitmqadapter.properties.RabbitMqBaseProperties;
import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerAuth;
import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerCreate;
import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerUpdate;
import co.com.bancolombia.rpcrabbitmq.gateways.RabbitToCognitoRepository;
import co.com.bancolombia.rpcserver.gateways.CalfRpcServerCreateRepository;
import co.com.bancolombia.secret.ServicesCredentials;
import co.com.bancolombia.useregister.UseRegisterUseCase;
import co.com.bancolombia.userregister.gateways.UserRegisterRepository;
import co.com.bancolombia.utill.AwsUtils;
import com.rabbitmq.client.Channel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ConfigurationProperties(prefix = "queue")
@Data
public class UseCasesConfig {
        @Bean
        public UseRegisterUseCase useRegisterUseCase(UserRegisterRepository userRegisterRepository){
                return new UseRegisterUseCase(userRegisterRepository);
        }
}

