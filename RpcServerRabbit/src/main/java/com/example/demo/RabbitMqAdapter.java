package com.example.demo;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.rpcserver.CalfRpcServerCreate;
import com.rabbitmq.client.Connection;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Log4j2
@Service
public class RabbitMqAdapter  {

    private final CalfRpcServerCreate calfRpcServerCreate;
   
    public RabbitMqAdapter(CalfRpcServerCreate calfRpcServerCreate){
        this.calfRpcServerCreate = calfRpcServerCreate;
    }

    @Autowired
    private Mono<Connection> connectionMono;

    
    public boolean consume() {

        
            Mono.just("Start thread create")
                    .publishOn(Schedulers.newParallel(""))
                    .map(msg -> {
                        try {
                            calfRpcServerCreate.mainloop();

                            return msg;

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .subscribe();

         return true;
        	 
        	 
        }
           

}
