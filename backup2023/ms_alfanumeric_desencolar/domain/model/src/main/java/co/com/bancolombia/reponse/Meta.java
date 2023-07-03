package co.com.bancolombia.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Meta {

    private final String messageId;
    private final String requestDate;
    private final int responseSize;
    private final String clientRequest;
}
