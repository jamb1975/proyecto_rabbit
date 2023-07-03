package co.com.bancolombia.security.alphanumericpassword.encryption.util;

import software.amazon.awssdk.core.SdkBytes;
import com.amazonaws.services.kms.model.EncryptResult;
import java.nio.ByteBuffer;
import org.springframework.util.Base64Utils;
import software.amazon.awssdk.services.kms.model.DecryptResponse;

public class Util {

    public static EncryptResult getEncryptedResultForTest() {
        return new EncryptResult().withCiphertextBlob(
            ByteBuffer.wrap(Base64Utils.decodeFromString("message")));
    }

    public static DecryptResponse getDecryptedResultForTest() {
        return DecryptResponse.builder()
            .plaintext(SdkBytes.fromUtf8String("messagc="))
            .build();
    }
}
