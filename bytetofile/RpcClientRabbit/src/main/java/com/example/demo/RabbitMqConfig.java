package com.example.demo;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.ChannelPool;
import reactor.rabbitmq.ChannelPoolFactory;
import reactor.rabbitmq.ChannelPoolOptions;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.RabbitFluxException;
import reactor.rabbitmq.ResourceManagementOptions;
import reactor.rabbitmq.RpcClient;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;
import reactor.util.retry.RetrySpec;

@Configuration
@Data


public class RabbitMqConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqAdapter.class);

    private  String queue = "test_canales";
   
    @Autowired
    AmqpAdmin amqpAdmin;
    
    @Autowired
    private  RabbitProperties properties;

    
    
    @Bean(name = "monoConnection")
     Mono<Connection> connectionMono(ConnectionFactory connectionFactory) throws NoSuchAlgorithmException, KeyManagementException {

       
        return Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit")).cache();
    }
    
    @Bean(name = "monoConnectionFactory")
    @Primary
    ConnectionFactory connectionFactoryMono() throws NoSuchAlgorithmException, KeyManagementException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(properties.getHost());
        connectionFactory.setPort(properties.getPort());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        connectionFactory.setChannelShouldCheckRpcResponseType(true);
        //connectionFactory.setRequestedHeartbeat(7);
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.useNio();
        if(properties.getSsl().getEnabled() != null){
            connectionFactory.useSslProtocol();
        }

       return connectionFactory;
    }
   
    /*
De forma predeterminada, los métodos Sender#send* abren un nuevo canal para cada llamada.
 Esto está bien para llamadas de larga duración, p. cuando el flujo de mensajes salientes
  es infinito. Para las cargas de trabajo en las que Sender#send* se llama a menudo para
  un flujo limitado y corto de mensajes, abrir un nuevo canal cada vez puede no ser óptimo.
 */
    @Bean
    public ChannelPool channelPool(Mono<Connection> connectionMono){

        ChannelPool channelPool = ChannelPoolFactory.createChannelPool(
                connectionMono,
                new ChannelPoolOptions().maxCacheSize(100)
        );

        return channelPool;

    }

    @Bean
    Sender sender(Mono<Connection> connectionMono,final ChannelPool channelPool, Mono<Channel> channelMono, ConnectionFactory connectionFactory) {
    /*
    Algunas aplicaciones pueden querer fallar rápidamente y lanzar una excepción cuando
    el intermediario no está disponible. Es posible que otras aplicaciones deseen volver
    a intentar abrir la conexión cuando falla.
     */
    	 ResourceManagementOptions options = new ResourceManagementOptions()
    	            .channelMono(channelMono); 
    	
         SenderOptions senderOptions = new SenderOptions().connectionFactory(connectionFactory)
                 .resourceManagementScheduler(Schedulers.boundedElastic());

        Sender sender = RabbitFlux.createSender(senderOptions
                .connectionMono(connectionMono)
                .connectionMonoConfigurator(cm ->
                        cm.retryWhen(RetrySpec.backoff(3, Duration.ofSeconds(5))  //
                        )).channelPool(channelPool));
        
         
        sender.declareQueue(QueueSpecification.queue(queue), options);
        return sender;
    }

   

    @Bean
    Mono<Channel> channel(Mono<Connection> connectionMono) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException {

     
        Mono<Channel> channelMono = connectionMono.map(c -> {
            try {
                return c.createChannel();
            } catch (Exception e) {
                throw new RabbitFluxException(e);
            }
        }).cache();      
      
        return channelMono;
    }
    
   @Bean
   public RpcClient rpcClient(final Mono<Channel> monoChannel ) {
	   
	   RpcClient rpcClient = new RpcClient(monoChannel, "", queue);
	   return rpcClient;
   }
    
}
