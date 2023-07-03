package co.com.bancolombia.rabbitmqadapter;

import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.cognito.signup.gateways.SignUpRepository;
import co.com.bancolombia.rabbitmqadapter.properties.RabbitMqBaseProperties;
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
import reactor.util.function.Tuples;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public   class CalfRpcServerCreate extends RpcServer  {


    public static final Gson GSON = new Gson();
    private static final Logger LOGGER = LoggerFactory.getLogger(CalfRpcServerCreate.class);
    @Autowired
    private SignUpRepository signUpFlowRabbit;
    @Autowired
    private RabbitMqBaseProperties rabbitMqBaseProperties;

    public CalfRpcServerCreate(Channel channel, String queueName) throws IOException {
        super(channel, queueName);
    }
    @Override
    public byte[] handleCall(Delivery request, AMQP.BasicProperties replyProperties) {
      byte[] reponse =  Mono.just(request)
                .map(req ->{
                    String pManagementRequest = new String(req.getBody(), StandardCharsets.UTF_8);
                    PasswordManagementRequest passwordManagementRequest = null;
                    passwordManagementRequest = GSON.fromJson(pManagementRequest, PasswordManagementRequest.class);
                    var  response = signUpFlowRabbit.signUp(passwordManagementRequest, passwordManagementRequest.getMessageId(), passwordManagementRequest.getConsumer());
                    LOGGER.info("response->{}", response);
                    return response;
                })
                .timeout(Duration.ofMillis(rabbitMqBaseProperties.getTimewait()))
                .retry(rabbitMqBaseProperties.getReintentos())
                .map(resp -> GSON.toJson(resp).getBytes(StandardCharsets.UTF_8)).share().block();
      return reponse;
        }


}
