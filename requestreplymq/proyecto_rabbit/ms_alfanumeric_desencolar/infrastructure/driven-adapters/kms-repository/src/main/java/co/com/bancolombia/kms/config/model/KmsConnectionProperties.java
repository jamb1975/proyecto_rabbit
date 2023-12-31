package co.com.bancolombia.kms.config.model;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "adapters.aws.kms")
public class KmsConnectionProperties {

    String host;
    String region;
    String protocol;
    Integer port;
    String keyId;

    public String getEndpoint() {
        return String.format("%s://%s:%s", protocol, host, port);
    }

}
