package co.com.bancolombia.rabbitmqadapter.rpcserver;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.rpcrabbitmq.gateways.RabbitToCognitoRepository;
import co.com.bancolombia.rpcserver.gateways.CalfRpcServerCreateRepository;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public   class  CalfRpcServerCreate extends RpcServer implements CalfRpcServerCreateRepository {


    public static final Gson GSON = new Gson();
    private final RabbitToCognitoRepository rabbitToCognitoRepository;
    public  CalfRpcServerCreate(Channel channel, String queueName, RabbitToCognitoRepository rabbitToCognitoRepository) throws IOException {
        super(channel, queueName);
        this.rabbitToCognitoRepository = rabbitToCognitoRepository;

    }
    @Override
    public byte[] handleCall(Delivery request, AMQP.BasicProperties replyProperties) {

        try{
         String input = new String(request.getBody());
         return signUp(new String(request.getBody(), StandardCharsets.UTF_8));
        }catch(Exception e){
            return  GSON.toJson(
                            ApiResponse.createOnError("NoValid"
                                    , "CALF"
                                    , Constants.PASSWORD_IS_NOT_VALID))
                    .getBytes(StandardCharsets.UTF_8);
        }

    }

    public byte[] signUp(String pManagementRequest){
        byte[] result = rabbitToCognitoRepository.signUp(pManagementRequest);
        if(result !=null) return result;
        else  return  GSON.toJson(
                        ApiResponse.createOnError("NoValid"
                                , "CALF"
                                , Constants.PASSWORD_IS_NOT_VALID))
                .getBytes(StandardCharsets.UTF_8);
    }




}
