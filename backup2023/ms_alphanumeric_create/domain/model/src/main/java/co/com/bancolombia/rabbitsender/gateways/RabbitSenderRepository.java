package co.com.bancolombia.rabbitsender.gateways;

import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.common.response.ApiResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface RabbitSenderRepository {
    Mono<ApiResponse> consumer(PasswordManagementRequest passwordManagementRequest) throws IOException, TimeoutException;
}
