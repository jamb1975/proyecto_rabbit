package co.com.bancolombia.cognito.ops;

import co.com.bancolombia.cognito.properties.CognitoBaseProperties;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderAsyncClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class InitAuthFlowTest {


    private InitAuthFlow initAuthFlow;
    private PasswordManagementRequest passwordManagementRequest;
    private final String messageId = "33c3dd38-73b8-44c9-b461-b4ca7835c801";
    private final String consumer = "CALF";

    private  CognitoBaseProperties cognitoBaseProperties;

    private  CognitoIdentityProviderAsyncClient asyncClient;
    @BeforeEach
    void setUp() {
        cognitoBaseProperties = mock(CognitoBaseProperties.class);
        asyncClient = mock(CognitoIdentityProviderAsyncClient.class);
        passwordManagementRequest = PasswordManagementRequest.builder().password("fbc8399c7d05656e506e8a2a7f3ed37aa3a5a24ad82b482ee5fcfca6aa451c8f").aid("AC0D2C3F4192A4F15A5FFC18AC24F09B6").messageId(messageId).consumer(consumer).build();
        initAuthFlow = new InitAuthFlow(cognitoBaseProperties, asyncClient);
    }


    @Test
    void startSignInFlow() {

       String result = initAuthFlow.startSignInFlow(passwordManagementRequest, messageId, consumer).block().getMeta().getMessageId();
        assertEquals(messageId, result);
    }

}
