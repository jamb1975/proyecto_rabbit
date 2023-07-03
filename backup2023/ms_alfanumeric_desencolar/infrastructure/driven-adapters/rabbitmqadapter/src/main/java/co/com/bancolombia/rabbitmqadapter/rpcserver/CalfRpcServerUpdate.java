package co.com.bancolombia.rabbitmqadapter.rpcserver;

import co.com.bancolombia.rpcrabbitmq.gateways.RabbitToCognitoRepository;
import co.com.bancolombia.rpcserver.gateways.CalfRpcServerUpdateRepository;
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

    public  CalfRpcServerUpdate(Channel channel, String queueName, RabbitToCognitoRepository rabbitToCognitoRepository) throws IOException {
        super(channel, queueName);
        this.rabbitToCognitoRepository = rabbitToCognitoRepository;
        LOGGER.info(" CALF Queue Update->{}", queueName);
    }
    @Override
    public byte[] handleCall(Delivery request, AMQP.BasicProperties replyProperties) {

        return update(new String(request.getBody(), StandardCharsets.UTF_8));

    }
    @Override
    public byte[] update(String pManagementRequest) {
       return rabbitToCognitoRepository.update(pManagementRequest);
    }

    @Override
    public void initMainLoop() {

    }
}
