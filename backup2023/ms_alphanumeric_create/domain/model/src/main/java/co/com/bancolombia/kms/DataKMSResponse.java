package co.com.bancolombia.kms;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class DataKMSResponse {

    private String type;
    private String id;
    private AttributesKMSResponse attributes;
}
