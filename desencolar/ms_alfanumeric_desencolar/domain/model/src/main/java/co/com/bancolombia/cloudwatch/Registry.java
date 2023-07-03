package co.com.bancolombia.cloudwatch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Registry {
    private String sourceId;
    private String actor;
    private String transactionalId;
    private String document;
    private String documentType;
}
