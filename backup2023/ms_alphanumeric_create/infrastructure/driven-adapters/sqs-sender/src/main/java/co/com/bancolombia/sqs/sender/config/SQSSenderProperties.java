package co.com.bancolombia.sqs.sender.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "adapter.sqs")
public class SQSSenderProperties {
    private String region;
    private String queueUrl;
    private String endpoint;
}
