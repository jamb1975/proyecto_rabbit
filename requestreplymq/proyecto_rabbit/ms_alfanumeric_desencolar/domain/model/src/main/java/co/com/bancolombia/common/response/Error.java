package co.com.bancolombia.common.response;


import co.com.bancolombia.cognito.signin.model.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Error {

    private final String id;
    private final int status;
    private final String code;
    private final String title;
    private final String detail;

    public Error(int status, String code, String detail) {
        this.id = UUID.randomUUID().toString();
        this.status = status;
        this.code = code;
        this.title = new String(Constants.FAILED_TRANSACTION_TITLE.getBytes(),
            StandardCharsets.UTF_8);
        this.detail = new String(detail.getBytes(), StandardCharsets.UTF_8);
    }
}
