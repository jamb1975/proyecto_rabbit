package co.com.bancolombia.cognito.signup.model;

import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder(toBuilder = true)
@Jacksonized
@Getter
public class SignUpVO extends PasswordManagementRequest {

    private final String email;

    @Override
    public String toString() {
        return "SignUpRequest{" +
            "email='" + email + '\'' +
            "} " + super.toString();
    }
}
