package co.com.bancolombia.cognito.signup.gateways;

import co.com.bancolombia.cognito.admin.OpsForUser;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.common.response.ApiResponse;

public interface SignUpRepository {

    ApiResponse signUp(PasswordManagementRequest signUpVO, String messageId,
                       String consumer);

    String deleteUser(OpsForUser opsForUser);
}
