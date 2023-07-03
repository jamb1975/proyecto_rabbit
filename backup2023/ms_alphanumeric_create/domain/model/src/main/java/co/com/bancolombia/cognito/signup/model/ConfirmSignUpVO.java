package co.com.bancolombia.cognito.signup.model;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Getter
@Jacksonized
public class ConfirmSignUpVO {

    private final String username;
    private final String confirmationCode;
}
