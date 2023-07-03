package co.com.bancolombia.cognito.signin.gateways;

import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface SignInRepository {

    Mono<ResponseEntity<ApiResponse>> startSignInFlow(
            PasswordManagementRequest passwordManagementRequest, String messageId,
            String consumer);
}

