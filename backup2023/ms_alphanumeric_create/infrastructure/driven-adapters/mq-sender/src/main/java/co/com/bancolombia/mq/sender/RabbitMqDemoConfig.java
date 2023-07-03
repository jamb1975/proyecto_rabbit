package co.com.bancolombia.mq.sender;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static com.rabbitmq.client.ConnectionFactory.DEFAULT_AMQP_OVER_SSL_PORT;

@Configuration
public class RabbitMqDemoConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqDemoConfig.class);
	private  String queue_auth;
	private  String queue_create;
	@Autowired
	private RabbitProperties properties;
	@Bean(name = "monoConnection")
	Mono<Connection> connectionMono() throws NoSuchAlgorithmException, KeyManagementException {

		com.rabbitmq.client.ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(properties.getHost());
		connectionFactory.setPort(properties.getPort());
		connectionFactory.setUsername(properties.getUsername());
		connectionFactory.setPassword(properties.getPassword());
		if(properties.getSsl().getEnabled() != null){
			connectionFactory.useSslProtocol();}
		return Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit")).cache();
	}
	@PostConstruct
	public void init() {
		LOGGER.info("Version 2.0");
		LOGGER.info("Port Amqps->{}",DEFAULT_AMQP_OVER_SSL_PORT);
		LOGGER.info("Host->{}", properties.getHost());
		LOGGER.info("Port->{}", properties.getPort());
		LOGGER.info("Username->{}", properties.getUsername());
		LOGGER.info("Password->{}", properties.getPassword());
		LOGGER.info("VirtualHost->{}", properties.getVirtualHost());
		LOGGER.info("ssl->{}", properties.getSsl().getEnabled());
		LOGGER.info("queue_create->{}", queue_create);
		LOGGER.info("queue_auth->{}", queue_auth);
	}
}
