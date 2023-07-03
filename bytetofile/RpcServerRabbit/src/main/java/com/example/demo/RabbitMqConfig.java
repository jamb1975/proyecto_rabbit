package com.example.demo;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.example.demo.rpcserver.CalfRpcServerCreate;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Data;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.ChannelPool;
import reactor.rabbitmq.ChannelPoolFactory;
import reactor.rabbitmq.ChannelPoolOptions;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;

@Configuration
@Data
public class RabbitMqConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqAdapter.class);
   
    private String queue = "test_canales";
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
         channel.queueDeclare(queue, true, false, false, null);
         

        return channel;
    }

    @Bean
    Sender sender(Mono<Connection> connectionMono) {
        return RabbitFlux.createSender(new SenderOptions().connectionMono(connectionMono));
    }
    
    @Bean
    public CalfRpcServerCreate calfRpcServerCreate(Channel channel) throws IOException {
        return new CalfRpcServerCreate(channel, queue);
    }

    @Bean
    public ChannelPool channelPool(Mono<Connection> connectionMono){

        ChannelPool channelPool = ChannelPoolFactory.createChannelPool(
                connectionMono,
                new ChannelPoolOptions().maxCacheSize(100)
        );

        return channelPool;

    }
   
}
