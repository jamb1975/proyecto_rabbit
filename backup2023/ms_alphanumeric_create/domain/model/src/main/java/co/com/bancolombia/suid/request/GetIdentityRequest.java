package co.com.bancolombia.suid.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetIdentityRequest {

    private DataGetIdentityRequest[] data;
}
