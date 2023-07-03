package co.com.bancolombia.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SecretDBModel {

    private String username;
    private String password;
    private String host;
    private String port;
    private String dbname;
    private String engine;
    private String dbInstanceIdentifier;
}
