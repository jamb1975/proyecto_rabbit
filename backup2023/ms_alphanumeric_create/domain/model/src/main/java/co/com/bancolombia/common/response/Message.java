package co.com.bancolombia.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Message {

    private final String source;
    private final String detail;
    private final String type;
    private final String title;
    private final int status;
}
