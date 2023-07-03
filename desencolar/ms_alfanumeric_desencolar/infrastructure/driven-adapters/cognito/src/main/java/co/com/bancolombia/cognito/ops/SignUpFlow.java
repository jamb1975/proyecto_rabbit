package co.com.bancolombia.cognito.ops;

import co.com.bancolombia.admin.OpsForUser;
import co.com.bancolombia.cognito.properties.CognitoBaseProperties;
import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.cognito.signup.gateways.SignUpRepository;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.common.response.Data;
import co.com.bancolombia.common.util.PathConstants;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

@Service
@Slf4j
public class SignUpFlow implements SignUpRepository {

    private final CognitoIdentityProviderClient client;
    private final String appClientId;
    private final CognitoBaseProperties cognitoBaseProperties;
    public SignUpFlow(CognitoBaseProperties cognitoBaseProperties,
        CognitoIdentityProviderClient cognitoClient) {
        this.cognitoBaseProperties = cognitoBaseProperties;
        this.client = cognitoClient;
        this.appClientId = this.cognitoBaseProperties.getAppClientId();
    }
    @Override
    public ApiResponse signUp(PasswordManagementRequest passwordManagementRequest, String messageId,
        String consumer) {
        var iniCognito = System.currentTimeMillis();
        String secretHash = new HashingSecret(cognitoBaseProperties).hashing(
            passwordManagementRequest.getAid());
        try {
            client.signUp(signUpBuilder -> signUpBuilder.clientId(appClientId)
                .username(passwordManagementRequest.getAid())
                .password(passwordManagementRequest.getPassword())
                .secretHash(secretHash)
            );

            AdminUpdateUserAttributesRequest.builder()
                .userPoolId(cognitoBaseProperties.getUserPoolId())
                .username(passwordManagementRequest.getAid())
                .userAttributes(attributeTypes(Map.of("email_verified", "true")))
                .build();

            var adminConfirmSignUpRequest = AdminConfirmSignUpRequest.builder()
                .userPoolId(cognitoBaseProperties.getUserPoolId())
                .username(passwordManagementRequest.getAid())
                .build();

            AdminConfirmSignUpResponse confirmation = getAdminConfirmSignUpResponse(
                adminConfirmSignUpRequest);

            if (confirmation.sdkHttpResponse().isSuccessful()) {
                  return ApiResponse.createOnSuccess(messageId, consumer, Constants.ONE)
                    .setMessage(PathConstants.CREATE_PASSWORD,
                        Constants.PASSWORD_CREATE_SUCCESS_DETAIL,
                        Constants.CREATE_SUCCESS_STATUS)
                    .setData(new Data[0]);
            } else {
                 log.info(
                    "Confirmacion usuario no completada: " + adminConfirmSignUpRequest.username()
                        + " UserPoolId: " + adminConfirmSignUpRequest.userPoolId());
                return ApiResponse.createOnError(messageId, consumer, Constants.INVALID_IDENTIFIER);

            }
        } catch (Exception e) {
             log.error("Registro de usuario no completado: " + passwordManagementRequest.getAid()
                + " Error: "
                + e.getMessage());
            return ApiResponse.createOnError(messageId, consumer,
                Constants.PASSWORD_ALREADY_EXISTS);
        }
    }



    private AdminConfirmSignUpResponse getAdminConfirmSignUpResponse(
        AdminConfirmSignUpRequest adminConfirmSignUpRequest) {

        int retry = 0;
        AdminConfirmSignUpResponse varAdmin = client.adminConfirmSignUp(adminConfirmSignUpRequest);
        while (!varAdmin.sdkHttpResponse().isSuccessful() && retry <= 3) {

            varAdmin = client.adminConfirmSignUp(adminConfirmSignUpRequest);
            retry = retry + 1;
            log.info(
                "Reintentando Usuario:" + adminConfirmSignUpRequest.username() + " UserPoolId: "
                    + adminConfirmSignUpRequest.userPoolId());
        }
        return varAdmin;
    }

    public Collection<AttributeType> attributeTypes(Map<String, String> attributesAndValues) {
        Set<AttributeType> attributeTypes = new HashSet<>();
        attributesAndValues.forEach(
            (k, v) -> attributeTypes.add(AttributeType.builder().name(k).value(v).build()));
        return attributeTypes;
    }
}
