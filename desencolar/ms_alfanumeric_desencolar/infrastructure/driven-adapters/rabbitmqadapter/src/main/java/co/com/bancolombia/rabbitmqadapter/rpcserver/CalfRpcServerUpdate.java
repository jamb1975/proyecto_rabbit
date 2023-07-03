package co.com.bancolombia.rabbitmqadapter.rpcserver;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.rpcrabbitmq.gateways.RabbitToCognitoRepository;
import co.com.bancolombia.rpcserver.gateways.CalfRpcServerUpdateRepository;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public   class CalfRpcServerUpdate extends RpcServer implements CalfRpcServerUpdateRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalfRpcServerCreate.class);
    private final RabbitToCognitoRepository rabbitToCognitoRepository;
    public static final Gson GSON = new Gson();
    public  CalfRpcServerUpdate(Channel channel, String queueName, RabbitToCognitoRepository rabbitToCognitoRepository) throws IOException {
        super(channel, queueName);
        this.rabbitToCognitoRepository = rabbitToCognitoRepository;

    }
    @Override
    public byte[] handleCall(Delivery request, AMQP.BasicProperties replyProperties) {
        try{
            return update(new String(request.getBody(), StandardCharsets.UTF_8));
        }catch(Exception e){
            return  GSON.toJson(
                            ApiResponse.createOnError("NoValid"
                                    , "CALF"
                                    , Constants.PASSWORD_IS_NOT_VALID))
                    .getBytes(StandardCharsets.UTF_8);
        }
    }
    @Override
    public byte[] update(String pManagementRequest) {

        byte[] result = rabbitToCognitoRepository.update(pManagementRequest);

        if(result !=null) return result;
        else  return  GSON.toJson(
                        ApiResponse.createOnError("NoValid"
                                , "CALF"
                                , Constants.PASSWORD_IS_NOT_VALID))
                .getBytes(StandardCharsets.UTF_8);
    }


}
