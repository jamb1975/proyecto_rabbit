package co.com.bancolombia.kms.gateways;

public interface AsymmetricKmsAdapter {

    String decryptWithKms(String encryptedPart);

    String encryptWithKms(String encryptedPart);

}
