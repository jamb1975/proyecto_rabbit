package co.com.bancolombia.rabbitmqadapter;

import co.com.bancolombia.rabbitmqadapter.config.RabbitMqConfig;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.concurrent.TimeoutException;



public class RabbitMqAdapterTest {


    //@InjectMocks
   // private CalfRpcServerCreate calfRpcServerCreate;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqAdapterTest.class);
    private static final long RECOVERY_INTERVAL = 2000;

    @Mock
   private ConnectionFactory connectionFactory;
    @InjectMocks
    private RabbitMqConfig rabbitMqConfig;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    //@Test
    public void consumeTest() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.useNio();
        connectionFactory.setNetworkRecoveryInterval(RECOVERY_INTERVAL);connectionFactory.setNetworkRecoveryInterval(RECOVERY_INTERVAL);
   LOGGER.info("Conexion->{}", connectionFactory.newConnection());

       String result = "result";
                /* Mono.just("Start thread create")
                .publishOn(Schedulers.newParallel("thread-create"))
                .map(msg -> {

                    try {
                        calfRpcServerCreate.mainloop();
                        return msg;

                    } catch (IOException e) {
                        return "Error Listener RpcServer";
                    }
                }).block();
            */
       Assertions.assertEquals("queue_calf_create", rabbitMqConfig.getQueue_auth() );


    }
}
