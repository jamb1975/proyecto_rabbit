package co.com.bancolombia.cloudwatch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Message {

    private Details details;
    private Message message;
    private Registry registry;
    private Time time;
    private  Type type;
    private String additionalInfo;

}
