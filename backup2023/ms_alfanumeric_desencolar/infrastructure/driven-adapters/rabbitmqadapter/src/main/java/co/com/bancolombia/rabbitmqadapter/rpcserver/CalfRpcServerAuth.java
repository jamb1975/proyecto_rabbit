package co.com.bancolombia.rabbitmqadapter.rpcserver;

import co.com.bancolombia.cognito.signin.gateways.SignInRepository;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.rabbitmqadapter.properties.RabbitMqBaseProperties;
import co.com.bancolombia.rpcrabbitmq.gateways.RabbitToCognitoRepository;
import co.com.bancolombia.rpcserver.gateways.CalfRpcServerAuthRepository;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;


public   class CalfRpcServerAuth extends RpcServer implements CalfRpcServerAuthRepository {


    public static final Gson GSON = new Gson();
    private static final Logger LOGGER = LoggerFactory.getLogger(CalfRpcServerAuth.class);
    private final RabbitToCognitoRepository rabbitToCognitoRepository;
    public CalfRpcServerAuth(Channel channel, String queueName, RabbitToCognitoRepository rabbitToCognitoRepository) throws IOException {
        super(channel, queueName);
        this.rabbitToCognitoRepository = rabbitToCognitoRepository;
        LOGGER.info(" CALF Queue Auth->{}", queueName);
    }
    @Override
    public byte[] handleCall(Delivery request, AMQP.BasicProperties replyProperties) {

                 LOGGER.info("request auth->{}", new String(request.getBody(), StandardCharsets.UTF_8));
                 String pManagementRequest = new String(request.getBody(), StandardCharsets.UTF_8);
                 return  signUp(pManagementRequest);
    }
    @Override
    public byte[] signUp(String pManagementRequest) {
        return rabbitToCognitoRepository.signIn(pManagementRequest);
    }

    @Override
    public void initMainLoop() {

    }
}
