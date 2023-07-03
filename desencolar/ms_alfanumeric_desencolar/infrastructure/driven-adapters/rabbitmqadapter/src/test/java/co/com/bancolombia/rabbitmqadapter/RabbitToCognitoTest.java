package co.com.bancolombia.rabbitmqadapter;

import co.com.bancolombia.cognito.signin.gateways.SignInRepository;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.cognito.signup.gateways.SignUpRepository;
import co.com.bancolombia.cognito.update.gateways.UpdateRepository;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.logscloudwatch.AsyncLogsCloudWatchUseCase;
import co.com.bancolombia.rabbitmqadapter.properties.RabbitMqBaseProperties;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@Log4j2
public class RabbitToCognitoTest {

    private  SignUpRepository signUpFlowRabbit;
    private  SignInRepository signInFlowRabbit;
    private  UpdateRepository updateFlowRabbit;
    private  RabbitMqBaseProperties rabbitMqBaseProperties;
    private  AsyncLogsCloudWatchUseCase asyncLogsCloudWatchUseCase;
    private RabbitToCognito rabbitToCognito;
    private String passwordManagementRequest;
    private final String messageId = "NoValid";
    private final String consumer = "CALF";

    public static final Gson GSON = new Gson();

    @BeforeEach
    public void setUp(){

        signUpFlowRabbit = mock(SignUpRepository.class);
        signInFlowRabbit = mock(SignInRepository.class);
        updateFlowRabbit = mock(UpdateRepository.class);
        rabbitMqBaseProperties = mock(RabbitMqBaseProperties.class);
        asyncLogsCloudWatchUseCase = mock(AsyncLogsCloudWatchUseCase.class);
        rabbitToCognito = new RabbitToCognito(signUpFlowRabbit,
                signInFlowRabbit,
                updateFlowRabbit,
                rabbitMqBaseProperties,
                asyncLogsCloudWatchUseCase);
        passwordManagementRequest = GSON.toJson(PasswordManagementRequest.builder().password("fbc8399c7d05656e506e8a2a7f3ed37aa3a5a24ad82b482ee5fcfca6aa451c8f").aid("AC0D2C3F4192A4F15A5FFC18AC24F09B6").messageId(messageId).consumer(consumer).build());


    }

    @Test
    public void signUp() throws IOException, InterruptedException {
        String pManagementRequest = new String(rabbitToCognito.signUp(passwordManagementRequest), StandardCharsets.UTF_8);
        ApiResponse apiResponse = GSON.fromJson(pManagementRequest, ApiResponse.class);
        assertEquals(messageId, apiResponse.getMeta().getMessageId() );
    }

    @Test
    public void signIn() throws IOException, InterruptedException {
        String pManagementRequest = new String(rabbitToCognito.signIn(passwordManagementRequest), StandardCharsets.UTF_8);
         ApiResponse apiResponse = GSON.fromJson(pManagementRequest, ApiResponse.class);
         assertEquals(messageId, apiResponse.getMeta().getMessageId() );
    }

    @Test
    public void update() throws IOException, InterruptedException {
        String pManagementRequest = new String(rabbitToCognito.update(passwordManagementRequest), StandardCharsets.UTF_8);
        ApiResponse apiResponse = GSON.fromJson(pManagementRequest, ApiResponse.class);
        assertEquals(messageId, apiResponse.getMeta().getMessageId() );
    }
}
