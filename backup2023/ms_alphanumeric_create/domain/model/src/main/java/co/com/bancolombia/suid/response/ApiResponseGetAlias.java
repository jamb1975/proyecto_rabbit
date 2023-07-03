package co.com.bancolombia.suid.response;

import co.com.bancolombia.common.response.Message;
import co.com.bancolombia.reponse.Meta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ApiResponseGetAlias {

    private Meta meta;
    private DataGetAlias[] data;
    private Message message;
    private Error[] errors;
}
