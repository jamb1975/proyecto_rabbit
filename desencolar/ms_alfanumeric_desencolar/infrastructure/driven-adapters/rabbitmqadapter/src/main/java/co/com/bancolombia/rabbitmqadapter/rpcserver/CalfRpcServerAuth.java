package co.com.bancolombia.rabbitmqadapter.rpcserver;

import co.com.bancolombia.cognito.signin.gateways.SignInRepository;
import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.rabbitmqadapter.properties.RabbitMqBaseProperties;
import co.com.bancolombia.rpcrabbitmq.gateways.RabbitToCognitoRepository;
import co.com.bancolombia.rpcserver.gateways.CalfRpcServerAuthRepository;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.RpcServer;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Log4j2
public   class CalfRpcServerAuth extends RpcServer implements CalfRpcServerAuthRepository {


    public static final Gson GSON = new Gson();
    private final RabbitToCognitoRepository rabbitToCognitoRepository;
    public CalfRpcServerAuth(Channel channel, String queueName, RabbitToCognitoRepository rabbitToCognitoRepository) throws IOException {
        super(channel, queueName);
        this.rabbitToCognitoRepository = rabbitToCognitoRepository;
    }
    @Override
    public byte[] handleCall(Delivery request, AMQP.BasicProperties replyProperties) {
                 try {
                     String pManagementRequest = new String(request.getBody(), StandardCharsets.UTF_8);
                     return signIn(pManagementRequest);
                 }catch(Exception e){
                   return  GSON.toJson(
                                     ApiResponse.createOnError("NoValid"
                                             , "CALF"
                                             , Constants.PASSWORD_IS_NOT_VALID))
                             .getBytes(StandardCharsets.UTF_8);
                 }
    }
    @Override
    public byte[] signIn(String pManagementRequest) {

        byte[]  result = rabbitToCognitoRepository.signIn(pManagementRequest);
      if(result !=null) return result;
       else  return  GSON.toJson(
                            ApiResponse.createOnError("NoValid"
                                    , "CALF"
                                    , Constants.PASSWORD_IS_NOT_VALID))
                    .getBytes(StandardCharsets.UTF_8);

    }


}
