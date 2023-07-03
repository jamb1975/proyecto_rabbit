package co.com.bancolombia.cognito.ops;

import co.com.bancolombia.cognito.properties.CognitoBaseProperties;
import co.com.bancolombia.cognito.signin.gateways.SignInRepository;
import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.cognito.update.gateways.UpdateRepository;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.common.response.Data;
import co.com.bancolombia.common.util.PathConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderAsyncClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChangePasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChangePasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

@Service
public class UpdateFlow implements UpdateRepository {

    private final CognitoBaseProperties cognitoBaseProperties;
    private final CognitoIdentityProviderAsyncClient asyncClient;
    private final String appClientId;


    public UpdateFlow(CognitoBaseProperties cognitoBaseProperties,
                        CognitoIdentityProviderAsyncClient asyncClient) {
        this.cognitoBaseProperties = cognitoBaseProperties;
        this.asyncClient = asyncClient;
        this.appClientId = this.cognitoBaseProperties.getAppClientId();
    }

    @Override
    public Mono<ResponseEntity<ApiResponse>> startUpdateFlow(
            PasswordManagementRequest passwordManagementRequest, String messageId,
            String consumer) {
        var iniCognito = System.currentTimeMillis();
        UnaryOperator<String> hashed = usrName -> new HashingSecret(cognitoBaseProperties).hashing(
                usrName);
        var mutable = new HashMap<>(passwordManagementRequest.asParameters());
        mutable.putIfAbsent("SECRET_HASH", hashed.apply(passwordManagementRequest.getAid()));

        var resp = asyncClient.initiateAuth(
                authBuilder -> authBuilder.authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                        .authParameters(Map.copyOf(mutable))
                        .clientId(appClientId));
        var finCognito = System.currentTimeMillis();

        System.out.println("Tiempo de Cognito: " + (finCognito - iniCognito) / 1000F);
        return Mono.fromFuture(resp)
                .map(InitiateAuthResponse::authenticationResult)
                .map(rtaAuth -> ChangePasswordRequest.builder()
                        .accessToken(rtaAuth.accessToken())
                        .previousPassword(passwordManagementRequest.getPassword())
                        .proposedPassword(passwordManagementRequest.getProposedPassword())
                        .build()
                )
                .map(asyncClient::changePassword)
                .map(result -> ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.createOnSuccess(messageId, consumer, 1)
                                .setMessage(PathConstants.UPDATE_PASSWORD, Constants.PASSWORD_UPDATE_SUCCESS_DETAIL,
                                        Constants.UPDATE_SUCCESS_STATUS)
                                .setData(new Data[0])))
                .onErrorReturn(ResponseEntity.badRequest().body(
                        ApiResponse.createOnError(messageId, consumer, Constants.PASSWORD_IS_NOT_VALID)));
    }
}
