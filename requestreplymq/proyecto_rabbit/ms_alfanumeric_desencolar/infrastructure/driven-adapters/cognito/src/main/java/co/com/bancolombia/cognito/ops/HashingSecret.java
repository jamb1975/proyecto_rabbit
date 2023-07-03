package co.com.bancolombia.cognito.ops;

import co.com.bancolombia.cognito.properties.CognitoBaseProperties;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import co.com.bancolombia.exception.InvalidHashingException;

public class HashingSecret {

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private final CognitoBaseProperties cognitoBaseProperties;

    public HashingSecret(CognitoBaseProperties cognitoBaseProperties) {
        this.cognitoBaseProperties = cognitoBaseProperties;
    }

    public String hashing(String username) {
        var signingKey = new SecretKeySpec(
            cognitoBaseProperties.getAppClientSecret().getBytes(StandardCharsets.UTF_8),
            HMAC_SHA256_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            mac.update(username.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(
                cognitoBaseProperties.getAppClientId().getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new InvalidHashingException("Falla en la creación del hash", e);
        }
    }
}
