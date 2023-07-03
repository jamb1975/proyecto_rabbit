package co.com.bancolombia.rabbitmqadapter.config;

import co.com.bancolombia.rabbitmqadapter.CalfRpcServerAuth;
import co.com.bancolombia.rabbitmqadapter.CalfRpcServerCreate;
import co.com.bancolombia.rabbitmqadapter.RabbitMqAdapter;
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
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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

        return Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit")).cache();
    }
    @Bean
    Sender sender(Mono<Connection> connectionMono) {
        return RabbitFlux.createSender(new SenderOptions().connectionMono(connectionMono));
    }



    @Bean(name = "rpcServerCreate")
    @Qualifier("rpcServerCreate")
    CalfRpcServerCreate rpcServerCreate(Channel serverChannel) throws IOException {
        LOGGER.info("Se referencia canal y cola-> Clase CalfRpcServerCreate para   Envio rta despues de Crear Cognito");
        return  new CalfRpcServerCreate(serverChannel, queue_create);

    }
    @Bean(name = "rpcServerAuth")
    @Qualifier("rpcServerAuth")
    CalfRpcServerAuth rpcServerAuth(Channel serverChannel) throws IOException {
        LOGGER.info("Se referencia canal y cola-> Clase  CalfRpcServerAuth para Envio rta despues de Autenticar Cognito");

        return new CalfRpcServerAuth(serverChannel, queue_auth);

    }

    @Bean
    Channel channel() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(properties.getHost());
        connectionFactory.setPort(properties.getPort());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        LOGGER.info("Nueva Conexion Canal->{}", connectionFactory.newConnection());
        Channel channel = connectionFactory.newConnection().createChannel();
        LOGGER.info("Se Crea Canal->{}", channel);
        return channel;
    }

    @Bean
    String queue() throws IOException {
        return this.queue_create;
    }


    @PostConstruct
    public void init() {
        LOGGER.info("Version 2.0");
        LOGGER.info("Host->{}", properties.getHost());
        LOGGER.info("Port->{}", properties.getPort());
        LOGGER.info("Username->{}", properties.getUsername());
        LOGGER.info("Password->{}", properties.getPassword());
        LOGGER.info("VirtualHost->{}", properties.getVirtualHost());
        LOGGER.info("ssl->{}", properties.getSsl().getEnabled());
        LOGGER.info("queue_create->{}", queue_create);
        LOGGER.info("queue_auth->{}", queue_auth);
        //Queue queueCreate = new Queue(queue_create, false, false,true);
        Queue queueCreate = new Queue(queue_create);

        amqpAdmin.declareQueue(queueCreate);
        //Queue queueAuth = new Queue(queue_auth, false, false,true);
        Queue queueAuth = new Queue(queue_auth);

        amqpAdmin.declareQueue(queueAuth);
        LOGGER.info("Declaracion de colas->{}", true);
    }





}
