package co.com.bancolombia.suid.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataIdentity {

    private AttributesIdentity attributes;
    private String id;
    private String type;
}
