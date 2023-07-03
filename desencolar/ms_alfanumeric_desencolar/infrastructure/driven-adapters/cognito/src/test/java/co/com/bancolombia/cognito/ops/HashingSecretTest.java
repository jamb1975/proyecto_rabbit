package co.com.bancolombia.cognito.ops;

import co.com.bancolombia.cognito.properties.CognitoBaseProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@EnableConfigurationProperties(value = CognitoBaseProperties.class)
@ActiveProfiles
public class HashingSecretTest {

    private HashingSecret hashingSecret;
    @Autowired
    private  CognitoBaseProperties cognitoBaseProperties;

    @BeforeEach
    void setUp() {
        hashingSecret = new HashingSecret(cognitoBaseProperties);
    }

    @Test
    void hashing() {
        String username = "jamb";
        String error = "Falla en la creación del hash";
        String result = hashingSecret.hashing(username);

        assertNotEquals(error, result);
    }

}
