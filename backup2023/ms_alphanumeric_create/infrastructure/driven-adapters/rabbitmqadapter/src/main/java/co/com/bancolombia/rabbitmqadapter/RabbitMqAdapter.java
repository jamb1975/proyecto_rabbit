package co.com.bancolombia.rabbitmqadapter;

import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.cognito.signup.gateways.SignUpRepository;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.rabbitsender.gateways.RabbitSenderRepository;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.RpcClient;
import reactor.rabbitmq.Sender;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Service
@Log4j2
@ConfigurationProperties(prefix = "queue")
@Data
public class RabbitMqAdapter implements RabbitSenderRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqAdapter.class);

    private String queue_create;
    public static final Gson GSON = new Gson();
    @Autowired
    private Sender sender;


    private final AtomicBoolean latchCompleted = new AtomicBoolean(false);

    @Autowired
    private SignUpRepository signUpRepository;


    @Override
    public Mono<ApiResponse> consumer(PasswordManagementRequest passwordManagementRequest) throws IOException, TimeoutException {
        LOGGER.info("QUEUE->{}", queue_create);

        Supplier<String> correlationIdSupplier = () -> UUID.randomUUID().toString();
        return sender.rpcClient("", queue_create, correlationIdSupplier)
                .rpc(Mono.just(new RpcClient.RpcRequest(GSON.toJson(passwordManagementRequest).getBytes(StandardCharsets.UTF_8))))
                //.timeout(Duration.ofMillis(500))
                //.retry(3)
                .flatMap(reply -> {
                    LOGGER.info("reply->{}", new String(reply.getBody(), StandardCharsets.UTF_8));

                    return Mono.just(GSON.fromJson(new String(reply.getBody(), StandardCharsets.UTF_8), ApiResponse.class)).log();
                });

    }

    public Mono<Boolean> checkCognito() {
        return Mono.just(Boolean.TRUE);
    }


}
