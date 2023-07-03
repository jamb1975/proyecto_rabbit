package co.com.bancolombia.cloudwatch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Details {

    private String description;
    private String afterValue;
    private String beforeValue;
    private String transactionResultCode;
    private String transactionResultDescription;
}
