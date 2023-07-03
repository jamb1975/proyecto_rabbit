package co.com.bancolombia.rabbitmqadapter.rpcserver;

import co.com.bancolombia.rpcrabbitmq.gateways.RabbitToCognitoRepository;
import co.com.bancolombia.rpcserver.gateways.CalfRpcServerCreateRepository;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(CalfRpcServerCreate.class);

    private final RabbitToCognitoRepository rabbitToCognitoRepository;
    public  CalfRpcServerCreate(Channel channel, String queueName, RabbitToCognitoRepository rabbitToCognitoRepository) throws IOException {
        super(channel, queueName);
        this.rabbitToCognitoRepository = rabbitToCognitoRepository;
        LOGGER.info(" CALF Queue Create->{}", queueName);
    }
    @Override
    public byte[] handleCall(Delivery request, AMQP.BasicProperties replyProperties) {

        LOGGER.info("request->{}", new String(request.getBody(), StandardCharsets.UTF_8));
        String input = new String(request.getBody());
        LOGGER.info("Recibido from  CalfRpcServerCreate->{}", input);

      //  return ("*** " + input + " ***").getBytes();
       return signUp(new String(request.getBody(), StandardCharsets.UTF_8));

    }

    public byte[] signUp(String pManagementRequest){
        return rabbitToCognitoRepository.signUp(pManagementRequest);
    }

    @Override
    public void initMainLoop() {

    }


}
