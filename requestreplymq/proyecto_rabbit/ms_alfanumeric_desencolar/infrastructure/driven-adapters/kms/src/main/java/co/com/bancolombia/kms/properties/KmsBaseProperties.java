package co.com.bancolombia.kms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aws.kms")
public class KmsBaseProperties {

    private String keyId;
}
