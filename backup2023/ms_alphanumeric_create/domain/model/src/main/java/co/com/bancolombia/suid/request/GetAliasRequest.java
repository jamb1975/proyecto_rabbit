package co.com.bancolombia.suid.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAliasRequest {

    private DataGetAliasRequest[] data;
}
