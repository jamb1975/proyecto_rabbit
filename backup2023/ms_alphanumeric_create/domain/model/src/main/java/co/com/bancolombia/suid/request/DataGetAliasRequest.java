package co.com.bancolombia.suid.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DataGetAliasRequest {

    private String cid;
    private String aid;
    private String numeroDocumento;
    private String tipoDocumento;
    private String alias;
    private String canal;
    private String estadoAlias;
}
