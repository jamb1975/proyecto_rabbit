package com.example.demo.rpcserver;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.RpcServer;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

@Log4j2
public   class  CalfRpcServerCreate extends RpcServer  {

    Logger LOGGER = LoggerFactory.getLogger(CalfRpcServerCreate.class);
  
    public static final Gson GSON = new Gson();
 
    public  CalfRpcServerCreate(Channel channel, String queueName) throws IOException {
        super(channel, queueName);
        
    }
    @Override
    public byte[] handleCall(Delivery request, AMQP.BasicProperties replyProperties) {
   
         String input = new String(request.getBody(), StandardCharsets.UTF_8);
		 LOGGER.info("Recibido_->{}", input );
         return "Recibido-".concat(input).getBytes();
   

    }

   




}
