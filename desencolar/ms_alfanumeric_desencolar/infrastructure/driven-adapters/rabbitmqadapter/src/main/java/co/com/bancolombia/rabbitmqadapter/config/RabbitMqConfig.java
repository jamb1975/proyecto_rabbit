package co.com.bancolombia.rabbitmqadapter.config;

import co.com.bancolombia.cognito.signin.gateways.SignInRepository;
import co.com.bancolombia.cognito.signup.gateways.SignUpRepository;
import co.com.bancolombia.cognito.update.gateways.UpdateRepository;
import co.com.bancolombia.logscloudwatch.AsyncLogsCloudWatchUseCase;
import co.com.bancolombia.rabbitmqadapter.RabbitToCognito;
import co.com.bancolombia.rabbitmqadapter.properties.RabbitMqBaseProperties;
import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerAuth;
import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerCreate;
import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerUpdate;
import co.com.bancolombia.rabbitmqadapter.RabbitMqAdapter;
import co.com.bancolombia.rpcrabbitmq.gateways.RabbitToCognitoRepository;
import co.com.bancolombia.userregister.gateways.UserRegisterRepository;
import com.rabbitmq.client.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Configuration
@ConfigurationProperties(prefix = "queue")
@Data
public class RabbitMqConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqAdapter.class);
    private  String queue_create;
    private  String queue_auth;
    private  String queue_update;
    @Autowired
    private AmqpAdmin amqpAdmin;
    @Autowired
    private  RabbitProperties properties;
    @Bean(name = "monoConnection")
    @Primary
    Mono<Connection> connectionMono() throws NoSuchAlgorithmException, KeyManagementException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(properties.getHost());
        connectionFactory.setPort(properties.getPort());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        connectionFactory.setRequestedHeartbeat(0);
        connectionFactory.useNio();
        if(properties.getSsl().getEnabled() != null){
            connectionFactory.useSslProtocol();
        }

        return Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit")).cache();
    }

    @Bean
    Channel channel( Mono<Connection> connectionMono) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException {

         Channel channel = connectionMono.block().createChannel();
         channel.queueDeclare(queue_create, true, false, false, null);
         channel.queueDeclare(queue_auth, true, false, false, null);
         channel.queueDeclare(queue_update, true, false, false, null);

        return channel;
    }
    @Bean
    Sender sender(Mono<Connection> connectionMono) {
        return RabbitFlux.createSender(new SenderOptions().connectionMono(connectionMono));
    }
    @Bean
    public CalfRpcServerCreate calfRpcServerCreate(RabbitToCognitoRepository rabbitToCognitoRepository,
                                                   Channel channel) throws IOException {
        return new CalfRpcServerCreate(channel, queue_create, rabbitToCognitoRepository);
    }

    @Bean
    public CalfRpcServerAuth calfRpcServerAuth(RabbitToCognitoRepository rabbitToCognitoRepository,
                                               Channel channel) throws IOException {
        return new CalfRpcServerAuth(channel, queue_auth, rabbitToCognitoRepository);
    }

    @Bean
    public CalfRpcServerUpdate calfRpcServerUpdate(RabbitToCognitoRepository rabbitToCognitoRepository,
                                                   Channel channel) throws IOException {
        return new CalfRpcServerUpdate(channel, queue_update, rabbitToCognitoRepository);
    }
    @Bean
    public UserRegisterRepository userRegisterRepository(CalfRpcServerCreate calfRpcServerCreate,
                                                         CalfRpcServerAuth calfRpcServerAuth,
                                                         CalfRpcServerUpdate calfRpcServerUpdate,
                                                         AsyncLogsCloudWatchUseCase asyncLogsCloudWatchUseCase){
        return new RabbitMqAdapter(calfRpcServerCreate, calfRpcServerAuth, calfRpcServerUpdate, asyncLogsCloudWatchUseCase);
    }

    @Bean
    public RabbitToCognitoRepository rabbitToCognitoRepository(SignUpRepository signUpRepository,
                                                               SignInRepository signInRepository,
                                                               UpdateRepository updateRepository,
                                                               RabbitMqBaseProperties rabbitMqBaseProperties,
                                                               AsyncLogsCloudWatchUseCase asyncLogsCloudWatchUseCase){

        return new RabbitToCognito(signUpRepository,
                signInRepository,
                updateRepository,
                rabbitMqBaseProperties,
                asyncLogsCloudWatchUseCase);
    }

}
