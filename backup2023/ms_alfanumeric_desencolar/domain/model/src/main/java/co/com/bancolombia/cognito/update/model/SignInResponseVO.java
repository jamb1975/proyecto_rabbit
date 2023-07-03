package co.com.bancolombia.cognito.update.model;


import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Jacksonized
@Getter
public class SignInResponseVO {

    private final String type;
    private final String accessToken;
    private final long expiresIn;
    private final String idToken;
    private final String refreshToken;

}
