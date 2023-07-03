package co.com.bancolombia.rabbitmqadapter;

import co.com.bancolombia.logscloudwatch.AsyncLogsCloudWatchUseCase;
import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerAuth;
import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerCreate;
import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerUpdate;
import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerUpdateTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RabbitMqAdapterTest {

    private  CalfRpcServerCreate calfRpcServerCreate;
    private  CalfRpcServerAuth calfRpcServerAuth;
    private CalfRpcServerUpdate calfRpcServerUpdate;
    private  AsyncLogsCloudWatchUseCase asyncLogsCloudWatchUseCase;
    private RabbitMqAdapter rabbitMqAdapter;

    @BeforeEach
    public void setUp(){

        calfRpcServerCreate = mock(CalfRpcServerCreate.class);
        calfRpcServerAuth = mock(CalfRpcServerAuth.class);
        calfRpcServerUpdate = mock(CalfRpcServerUpdate.class);
        asyncLogsCloudWatchUseCase = mock(AsyncLogsCloudWatchUseCase.class);
        rabbitMqAdapter = new RabbitMqAdapter(calfRpcServerCreate,
                                              calfRpcServerAuth,
                                              calfRpcServerUpdate,
                                              asyncLogsCloudWatchUseCase);



    }

    @Test
    public void consumer() throws IOException, InterruptedException {

        assertEquals(true, rabbitMqAdapter.consume());
    }
}
