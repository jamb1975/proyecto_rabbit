package co.com.bancolombia.rabbitmqadapter.rpcserver;

import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.rpcrabbitmq.gateways.RabbitToCognitoRepository;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@Log4j2
public class CalfRpcServerAuthTest {
    private  Channel channel;
    private  final String queueName ="calf_queue_create";
    private  RabbitToCognitoRepository rabbitToCognitoRepository;
    private CalfRpcServerAuth calfRpcServerAuth;
    private String passwordManagementRequest;
    private final String messageId = "NoValid";
    private final String consumer = "CALF";
    public static final Gson GSON = new Gson();

    private Delivery delivery;
    @BeforeEach
    public void setup() throws IOException {
        channel = mock(Channel.class);
        rabbitToCognitoRepository = mock(RabbitToCognitoRepository.class);
        calfRpcServerAuth = new CalfRpcServerAuth(channel, queueName, rabbitToCognitoRepository);
        passwordManagementRequest = GSON.toJson(PasswordManagementRequest.builder().password("fbc8399c7d05656e506e8a2a7f3ed37aa3a5a24ad82b482ee5fcfca6aa451c8f").aid("AC0D2C3F4192A4F15A5FFC18AC24F09B6").messageId(messageId).consumer(consumer).build());
        delivery = mock(Delivery.class);
    }

    @Test
    public void handleCall(){

        byte[] result =  calfRpcServerAuth.handleCall(delivery, null);
        String sApiresponse = new String(result, StandardCharsets.UTF_8);
        ApiResponse apiResponse = GSON.fromJson(sApiresponse, ApiResponse.class);
        assertEquals(messageId, apiResponse.getMeta().getMessageId());
    }

    @Test
    public void signIn(){
        byte[] result =  calfRpcServerAuth.signIn(passwordManagementRequest);
        String sApiresponse = new String(result, StandardCharsets.UTF_8);
        ApiResponse apiResponse = GSON.fromJson(sApiresponse, ApiResponse.class);
        assertEquals(messageId, apiResponse.getMeta().getMessageId());
    }


}
