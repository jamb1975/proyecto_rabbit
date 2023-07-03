package co.com.bancolombia.suid.response;

import co.com.bancolombia.common.response.Message;
import co.com.bancolombia.reponse.Meta;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseIdentity {

    private Meta meta;
    private DataIdentity[] data;
    private Message message;
    private Error[] errors;
}
