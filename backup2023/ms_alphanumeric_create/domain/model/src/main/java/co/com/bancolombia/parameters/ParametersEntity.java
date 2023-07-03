package co.com.bancolombia.parameters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ParametersEntity {

    private String id;
    private String consumer;
    private String parameterName;
    private String parameterValue;

}
