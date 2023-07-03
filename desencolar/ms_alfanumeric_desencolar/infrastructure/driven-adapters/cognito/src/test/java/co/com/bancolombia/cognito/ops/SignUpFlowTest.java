package co.com.bancolombia.cognito.ops;

import co.com.bancolombia.cognito.properties.CognitoBaseProperties;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@EnableConfigurationProperties(value = CognitoBaseProperties.class)
@ActiveProfiles
public class SignUpFlowTest {

    private SignUpFlow signUpFlow;
    private PasswordManagementRequest passwordManagementRequest;
    private final String messageId = "33c3dd38-73b8-44c9-b461-b4ca7835c801";
    private final String consumer = "CALF";
    @Autowired
    private CognitoBaseProperties cognitoBaseProperties;
    private  CognitoIdentityProviderClient client;

    @BeforeEach
    void setUp() {
        client = mock(CognitoIdentityProviderClient.class);
        passwordManagementRequest = PasswordManagementRequest.builder().password("fbc8399c7d05656e506e8a2a7f3ed37aa3a5a24ad82b482ee5fcfca6aa451c8f").aid("AC0D2C3F4192A4F15A5FFC18AC24F09B6").messageId(messageId).consumer(consumer).build();
        signUpFlow = new SignUpFlow(cognitoBaseProperties, client);
    }


    @Test
    void signUp() {

        String result = signUpFlow.signUp(passwordManagementRequest, messageId, consumer).getMeta().getMessageId();
        assertEquals(messageId, result);
    }
}
