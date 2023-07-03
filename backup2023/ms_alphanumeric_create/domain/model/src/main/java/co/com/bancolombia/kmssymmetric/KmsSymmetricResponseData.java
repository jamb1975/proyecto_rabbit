package co.com.bancolombia.kmssymmetric;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class KmsSymmetricResponseData {

    private String message;
    private String encryptedDataKey;
}
