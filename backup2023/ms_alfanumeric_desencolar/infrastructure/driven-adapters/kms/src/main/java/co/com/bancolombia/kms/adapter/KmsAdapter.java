package co.com.bancolombia.kms.adapter;


import co.com.bancolombia.kms.gateways.AsymmetricKmsAdapter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptResponse;
import software.amazon.awssdk.services.kms.model.EncryptionAlgorithmSpec;

@Service
public class KmsAdapter implements AsymmetricKmsAdapter {

    private final String kmsAlias;
    private final KmsClient kmsClient;

    @Autowired
    public KmsAdapter(KmsClient kmsClient,
        @Value("${aws.kms.asymmetricAlias}") String kmsAlias) {
        this.kmsAlias = String.format("%s/%s", "alias", kmsAlias);
        this.kmsClient = kmsClient;
    }

    @Override
    public String decryptWithKms(String encryptedPart) {
        byte[] bytes = Base64.getDecoder().decode(encryptedPart.getBytes(StandardCharsets.UTF_8));
        var rsaesOaepSha256 =
            EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256;
        var decryptRequest =
            DecryptRequest.builder()
                .ciphertextBlob(
                    SdkBytes.fromByteArray(
                        bytes))
                .keyId(kmsAlias)
                .encryptionAlgorithm(
                    rsaesOaepSha256)
                .build();
        var resp = kmsClient.decrypt(decryptRequest);
        return resp.plaintext().asUtf8String();
    }

    @Override
    public String encryptWithKms(String encryptedPart) {
        byte[] bytes = Base64.getDecoder().decode(encryptedPart.getBytes(StandardCharsets.UTF_8));

        EncryptRequest encryptRequest = EncryptRequest.builder()
            .keyId(kmsAlias).plaintext(SdkBytes.fromByteArray(
                bytes)).encryptionAlgorithm(
                EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256).build();
        EncryptResponse encryptResponse = this.kmsClient
            .encrypt(encryptRequest);
        return encryptResponse.ciphertextBlob().toString();

    }
}
