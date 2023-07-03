package co.com.bancolombia.cognito.signup.usecase.signup.gateway;

import co.com.bancolombia.cognito.admin.OpsForUser;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface SignUpUseCase {

    Mono<ResponseEntity<ApiResponse>> startSignUpFlow(PasswordManagementRequest passwordManagementRequest,
                                                      String messageId,
                                                      String consumer);

    String deleteUser(OpsForUser opsForUser);

}
