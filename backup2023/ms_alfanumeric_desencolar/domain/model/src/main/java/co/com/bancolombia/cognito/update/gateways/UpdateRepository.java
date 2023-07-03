package co.com.bancolombia.cognito.update.gateways;

import co.com.bancolombia.admin.OpsForUser;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface UpdateRepository {

    Mono<ResponseEntity<ApiResponse>> startUpdateFlow(
            PasswordManagementRequest passwordManagementRequest, String messageId,
            String consumer);
}
