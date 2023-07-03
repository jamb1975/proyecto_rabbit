package co.com.bancolombia.rabbitmqadapter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "cognito")
public class RabbitMqBaseProperties {

    private long timewait;
    private long reintentos;

}
