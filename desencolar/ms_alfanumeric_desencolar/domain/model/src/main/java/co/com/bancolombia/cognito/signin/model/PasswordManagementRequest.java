package co.com.bancolombia.cognito.signin.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.util.Map;

@SuperBuilder(toBuilder = true)
@Jacksonized
@Data
public class PasswordManagementRequest implements Serializable {

    private String aid;
    private String password;
    private String messageId;
    private String consumer;
    private String proposedPassword;
    @Override
    public String toString() {
        return "PasswordManagementRequest{" +
                "aid='" + aid + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Map<String, String> asParameters() {
        return Map.of("USERNAME", aid,
                "PASSWORD", password);
    }

}
