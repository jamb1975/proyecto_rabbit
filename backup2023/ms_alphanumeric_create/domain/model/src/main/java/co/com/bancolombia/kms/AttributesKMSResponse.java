package co.com.bancolombia.kms;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AttributesKMSResponse {

    private final String message;
}
