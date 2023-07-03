package co.com.bancolombia.security.alphanumericpassword.encryption.kms.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.bancolombia.kms.adapter.KmsAdapter;
import co.com.bancolombia.security.alphanumericpassword.encryption.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;

class KmsAdapterTest {

    private KmsAdapter kmsAdapter;

    private KmsClient kmsClient;

    @BeforeEach
    void setUp() {
        kmsClient = mock(KmsClient.class);
        kmsAdapter = new KmsAdapter(kmsClient, "kmsalias");
    }

    @Test
    void decrypt() {
        String message = "message";
        var arrayBytesMessage = Base64Utils.decodeFromString(message);
        var messageEncodedBase64 = Base64Utils.encodeToString(arrayBytesMessage);
        var decryptedResult = Util.getDecryptedResultForTest();
        when(kmsClient.decrypt(any(DecryptRequest.class))).thenReturn(decryptedResult);
        String result = kmsAdapter.decryptWithKms(message);
        verify(kmsClient).decrypt(any(DecryptRequest.class));
        assertEquals(messageEncodedBase64, result);
    }


}
