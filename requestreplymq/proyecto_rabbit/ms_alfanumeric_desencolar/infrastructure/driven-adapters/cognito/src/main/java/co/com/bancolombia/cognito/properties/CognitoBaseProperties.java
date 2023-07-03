package co.com.bancolombia.cognito.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@Configurable
@ConfigurationProperties(prefix = "aws.cognito")
public class CognitoBaseProperties {

    private String appClientId;
    private String appClientSecret;
    private String userPoolId;
}
