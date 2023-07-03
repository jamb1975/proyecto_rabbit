package co.com.bancolombia.cognito.ops;

import co.com.bancolombia.cognito.properties.CognitoBaseProperties;
import co.com.bancolombia.cognito.signin.gateways.SignInRepository;
import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.common.response.Data;
import co.com.bancolombia.common.util.PathConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderAsyncClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

@Service
public class InitAuthFlow implements SignInRepository {

    private final CognitoBaseProperties cognitoBaseProperties;
    private final CognitoIdentityProviderAsyncClient asyncClient;
    private final String appClientId;
    private static final Logger LOGGER = LoggerFactory.getLogger(InitAuthFlow.class);

    public InitAuthFlow(CognitoBaseProperties cognitoBaseProperties,
                        CognitoIdentityProviderAsyncClient asyncClient) {
        this.cognitoBaseProperties = cognitoBaseProperties;
        this.asyncClient = asyncClient;
        this.appClientId = this.cognitoBaseProperties.getAppClientId();
    }

    @Override
    public Mono<ApiResponse> startSignInFlow(
            PasswordManagementRequest passwordManagementRequest, String messageId,
            String consumer) {

        try {
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

            return Mono.fromFuture(resp)
                    .map(InitiateAuthResponse::authenticationResult)
                    .map(result -> ApiResponse.createOnSuccess(messageId, consumer, 1)
                            .setMessage(PathConstants.VALIDATE_PASSWORD, Constants.VALIDATED_PASSWORD,
                                    Constants.PROCESS_SUCCESS_STATUS)
                            .setData(new Data[0])).log("Peticion Procesada En Cognito OK")
                    .onErrorResume(e->{
                        LOGGER.info("CALF_DESENCOLAR_ERROR_STARTSIGNINFLOW_FROMFUTURE: {} ",e.getMessage(), InitAuthFlow.class );
                        return Mono.just(
                                ApiResponse.createOnError(messageId
                                        , consumer
                                        , Constants.PASSWORD_IS_NOT_VALID));

                    });

        }catch(Exception e){
            LOGGER.info("CALF_DESENCOLAR_ERROR_startSignInFlow_EXCEPTION {}:",e.getMessage(), InitAuthFlow.class );
            return Mono.just(
                    ApiResponse.createOnError(messageId
                            , consumer
                            , Constants.PASSWORD_IS_NOT_VALID));
        }
    }
}
