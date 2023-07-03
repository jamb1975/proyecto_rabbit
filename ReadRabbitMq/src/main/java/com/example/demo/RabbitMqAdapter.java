package com.example.demo;

import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Delivery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.Receiver;

@Service
public class RabbitMqAdapter {
	
	@Autowired
	private Receiver receiver;
	public void readRAbbitMq() {
		Flux<Delivery> inboundFlux = receiver
		        .consumeNoAck("backup");
		
		inboundFlux.flatMap(b ->{
			try (FileOutputStream fos = new FileOutputStream("C:\\proyecto_rabbit\\cloud.zip")) {
				   fos.write(b.getBody());
				   //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return Mono.just(true);
		}).subscribe();
		
	}

}
