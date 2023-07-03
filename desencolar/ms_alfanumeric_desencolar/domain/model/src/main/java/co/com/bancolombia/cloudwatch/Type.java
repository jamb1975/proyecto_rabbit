package co.com.bancolombia.cloudwatch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Type {

    private String classification;
    private String action;
    private String app;
    private String process;

}
