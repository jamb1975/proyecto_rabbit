package co.com.bancolombia.cognito.signin.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder(toBuilder = true)
@Jacksonized
@Data
public class PasswordManagementRequest {

    private String aid;
    private String password;
    private String messageId;
    private String consumer;
    private String timeStamp;



}
