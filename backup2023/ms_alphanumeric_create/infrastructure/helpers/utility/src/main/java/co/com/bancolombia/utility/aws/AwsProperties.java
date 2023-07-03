package co.com.bancolombia.utility.aws;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aws.session.credentials")
public class AwsProperties {

    private String accessKey;
    private String secretKey;
    private String sessionToken;
}
