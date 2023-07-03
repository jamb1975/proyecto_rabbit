package co.com.bancolombia.cognito.signin.model;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Jacksonized
@Getter
public class KmsVo {

    private final String keyId;
    private final String textPlain;
    private final String encryptedPart;
}
