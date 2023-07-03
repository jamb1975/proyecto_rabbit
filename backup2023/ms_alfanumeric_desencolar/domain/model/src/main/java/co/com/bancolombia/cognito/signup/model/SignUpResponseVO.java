package co.com.bancolombia.cognito.signup.model;


import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Jacksonized
@Getter
public class SignUpResponseVO {


    private final String destination;
    private final String deliveryMedium;
    private final String attributeName;

}
