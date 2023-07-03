package co.com.bancolombia.secret;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyStoreSecret {

    private String keystorePassword;
    private String keystoreName;
}