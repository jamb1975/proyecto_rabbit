package co.com.bancolombia.kms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsAsyncClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptResponse;
import software.amazon.awssdk.services.kms.model.EncryptionAlgorithmSpec;
@Component
    @RequiredArgsConstructor
public class KmsAdapter  // implements ModelRepository from domain
{
    @Value("${adapters.aws.kms.keyId}")
    private String keyId;
    private final KmsAsyncClient kmsAsyncClient;

    public Mono<byte[]> decrypt(String secretKey, String keyId) {
        return Mono.fromFuture(kmsAsyncClient.decrypt(getDecryptRequest(secretKey, keyId)))
        .map(DecryptResponse::plaintext)
        .map(SdkBytes::asByteArray)
        .switchIfEmpty(Mono.error(new Throwable("Error decrypt secret")));
    }

    public Mono<String> encrypt(byte[] secretKey) {
        return Mono.fromFuture(kmsAsyncClient.encrypt(getEncryptRequest(secretKey, keyId)))
        .map(EncryptResponse::ciphertextBlob)
        .map(SdkBytes::asByteArray)
        .map(Base64Utils::encodeToString)
        .switchIfEmpty(Mono.error(new Throwable("Error encrypt secret")));
    }

    private DecryptRequest getDecryptRequest(String secretKey, String keyId) {
        byte[] decodedSecretKey = Base64Utils.decodeFromString(secretKey);
        return DecryptRequest.builder()
        .ciphertextBlob(SdkBytes.fromByteArray(decodedSecretKey))
        .keyId(keyId)
        .build();
    }

    private EncryptRequest getEncryptRequest(byte[] secretKey, String keyId) {
        return EncryptRequest.builder()
        .plaintext(SdkBytes.fromByteArray(secretKey))
        .encryptionAlgorithm(EncryptionAlgorithmSpec.SYMMETRIC_DEFAULT)
        .keyId(keyId)
        .build();
    }
}
