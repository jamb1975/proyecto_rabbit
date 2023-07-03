package co.com.bancolombia.rabbitmqadapter.config;

import co.com.bancolombia.rabbitmqadapter.RabbitMqAdapter;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
@ConfigurationProperties(prefix = "queue")
@Data

public class RabbitMqConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqAdapter.class);

    private  String queue_create;
    @Autowired
    Mono<Connection> connectionMono;
    @Autowired
    AmqpAdmin amqpAdmin;
    @Autowired
    private  RabbitProperties properties;

    @Bean()
    Mono<Connection> connectionMono() throws NoSuchAlgorithmException, KeyManagementException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(properties.getHost());
        connectionFactory.setPort(properties.getPort());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        if(properties.getSsl().getEnabled())
        {    connectionFactory.useSslProtocol();}
        return Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit")).cache();
    }
    @Bean
    Sender sender(Mono<Connection> connectionMono) {
        return RabbitFlux.createSender(new SenderOptions().connectionMono(connectionMono));
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
        //Queue queueCreate = new Queue(queue_create, false, false,true);
        Queue queueCreate = new Queue(queue_create);

        amqpAdmin.declareQueue(queueCreate);
        //Queue queueAuth = new Queue(queue_auth, false, false,true);

        LOGGER.info("Declaracion de colas->{}", true);
    }

    @PreDestroy
    public void close() throws Exception {
        connectionMono.block().close();
    }


}
