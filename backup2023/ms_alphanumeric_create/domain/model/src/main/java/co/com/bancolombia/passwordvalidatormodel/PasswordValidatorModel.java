package co.com.bancolombia.passwordvalidatormodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class PasswordValidatorModel {

    private final String consumer;
    private String password;
}
