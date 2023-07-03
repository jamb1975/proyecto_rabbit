package co.com.bancolombia.secret;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicesCredentials {

    private Credentials suidService;
    private Credentials apiConnect;
    private KeyStoreSecret keyStore;
}
