package co.com.bancolombia.cognito.signin.gateways;

import co.com.bancolombia.cognito.signin.model.SignInResponseVO;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import reactor.core.publisher.Mono;

public interface SignInRepository {

    Mono<SignInResponseVO> startSignInFlow(PasswordManagementRequest passwordManagementRequest);
}
