package co.com.bancolombia.kms.adapter;


import co.com.bancolombia.kms.gateways.AsymmetricKmsAdapter;

import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
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
        byte[] blob = Base64.getDecoder().decode(encryptedPart);

        var decryptReq = DecryptRequest
                .builder()
                .ciphertextBlob(SdkBytes.fromByteArray(blob))
                .encryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256)
                .keyId(kmsAlias)
                .build();

        var response = kmsClient.decrypt(decryptReq);
        var plainText = response.plaintext().asUtf8String();
        return plainText;
    }

    @Override
    public String encryptWithKms(String encryptedPart) {
        var encryptRequest = EncryptRequest
                .builder()
                .encryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256)
                .keyId(kmsAlias)
                .plaintext(SdkBytes.fromUtf8String(encryptedPart))
                .build();

        var response = kmsClient.encrypt(encryptRequest);
        return Base64.getEncoder().encodeToString(response.ciphertextBlob().asByteArray());

    }
}
