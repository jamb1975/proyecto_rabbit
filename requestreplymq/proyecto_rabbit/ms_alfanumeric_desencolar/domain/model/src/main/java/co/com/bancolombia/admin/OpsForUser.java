package co.com.bancolombia.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Jacksonized
@Getter
public class OpsForUser {

    private final String username;
}
