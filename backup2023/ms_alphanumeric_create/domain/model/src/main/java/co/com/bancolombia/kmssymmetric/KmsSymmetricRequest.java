package co.com.bancolombia.kmssymmetric;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class KmsSymmetricRequest {

    private String message;
    private String encryptedDataKey;
}
