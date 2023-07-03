package co.com.bancolombia.suid.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DataGetIdentityRequest {

    private String cid;
    private String numeroDocumento;
    private String tipoDocumento;
    private String mdmCodigo;
    private String grupoID;
}
