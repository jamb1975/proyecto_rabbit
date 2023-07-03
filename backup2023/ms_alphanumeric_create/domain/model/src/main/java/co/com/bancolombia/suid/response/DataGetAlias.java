package co.com.bancolombia.suid.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class DataGetAlias {

    private AttributesGetAlias attributes;
    private String id;
    private String type;
}
