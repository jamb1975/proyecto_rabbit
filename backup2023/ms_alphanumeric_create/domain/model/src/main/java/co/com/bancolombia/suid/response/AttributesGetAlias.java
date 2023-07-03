package co.com.bancolombia.suid.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@NoArgsConstructor
public class AttributesGetAlias {

    private String aid;
    private String cid;
    private String alias;
    private String canal;
    private String estadoAlias;
    private String aliasRevocado;
    private String canalCreacion;
    private String fechaCreacion;
    private String canalModificacion;
    private String fechaModificacion;
    private String canalRevocado;
    private String fechaRevocado;
    private String tipoDocumento;
    private String numeroDocumento;
}
