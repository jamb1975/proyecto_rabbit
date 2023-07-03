package co.com.bancolombia.sqs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SqsMessage {
    private String messageId;
    private String receiptHandle;
    private String md5OfBody;
    private String body;
    private Map<String, String> attributes;
    private String md5OfMessageAttributes;
}
