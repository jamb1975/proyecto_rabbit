package co.com.bancolombia.savebackup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LogDataRequest {

    private final Type type;
    private final Registry registry;
    private final Details details;
    private final Object additionalInfo;
}
