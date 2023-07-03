package com.example.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.ChannelPool;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.RpcClient;
import reactor.rabbitmq.Sender;
import reactor.util.function.Tuples;

@Service
@Log4j2
@Data
public class RabbitMqAdapter  {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqAdapter.class);

    private String queue = "test_canales";
    
    public static final Gson GSON = new Gson();
  
    @Autowired
    private Sender sender;

    @Autowired
    private Mono<Channel>monoChannel;
    
    @Autowired
    private ChannelPool channelPool;
    
    @Autowired
    private RpcClient rpcClient;

    private final AtomicBoolean latchCompleted = new AtomicBoolean(false);
    
    public Mono<String> sendMessageToSignUp(String passwordManagementRequest) throws IOException {
    	// channel.queueDeclare(queue, true, false, false, null);
        Supplier<String> correlationIdSupplier = () -> UUID.randomUUID().toString();
   
        return Mono.just(Tuples.of(GSON.toJson(passwordManagementRequest).getBytes(StandardCharsets.UTF_8), queue))
                //.publishOn(Schedulers.newParallel("Flujo a occccccgnito", 7))
        		.publishOn(Schedulers.boundedElastic())
        		//.flatMap(t2queuename ->  Mono.just(sender.rpcClient("", t2queuename.getT2(), correlationIdSupplier))
               .flatMap(t2queuename ->  Mono.just(rpcClient)
                               
                                .flatMap(rpc ->
                                    rpc.rpc(Mono.just(new RpcClient.RpcRequest(t2queuename.getT1())))
                                  // .timeout(Duration.ofMillis(timeout))
                                   .retry(7)
                                    .flatMap(reply -> {
                                   
                                    	                           
                                         putLogsCloudWatch("La cola envio una respuesta: {}", new String(reply.getBody(), StandardCharsets.UTF_8), RabbitMqAdapter.class);
                                        
                                        return Mono.just(new String(reply.getBody(), StandardCharsets.UTF_8)).log();
                                    })
                                    .subscribeOn(Schedulers.boundedElastic())
                                )
                    );
    }

  
    public static boolean putLogsCloudWatch(String id, String mesage, Class<?> sourceClass){
        Mono.just(Tuples.of(id, mesage, sourceClass))
                .publishOn(Schedulers.newParallel("Async With Thread Log Cloudwatch"))
                .map(t3IdMsgSourceClass -> {
                    Logger log = LoggerFactory.getLogger(t3IdMsgSourceClass.getT3());
                    log.info(t3IdMsgSourceClass.getT1().concat(": {}"), t3IdMsgSourceClass.getT2());
                    return true;
                })
                .subscribe();
        return true;
    }
    
    Mono<Delivery> rpc() {
        // tag::rpc[]
        String queue = "rpc.server.queue";
       
        RpcClient rpcClient = sender.rpcClient("", queue);  // <1>
        Mono<Delivery> reply = rpcClient.rpc(Mono.just(
            new RpcClient.RpcRequest("hello".getBytes())    // <2>
        ));
        reply.subscribe();
        rpcClient.close(); 
        
      return reply;  // <3>
        // end::rpc[]
    }
}