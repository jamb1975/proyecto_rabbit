package co.com.bancolombia.rabbitmqadapter;

import co.com.bancolombia.cognito.signin.gateways.SignInRepository;
import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.cognito.signup.gateways.SignUpRepository;
import co.com.bancolombia.cognito.update.gateways.UpdateRepository;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.logscloudwatch.AsyncLogsCloudWatchUseCase;
import co.com.bancolombia.rabbitmqadapter.properties.RabbitMqBaseProperties;
import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerCreate;
import co.com.bancolombia.rpcrabbitmq.gateways.RabbitToCognitoRepository;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Log4j2
public class RabbitToCognito implements RabbitToCognitoRepository {

    private final SignUpRepository signUpFlowRabbit;
    private final SignInRepository signInFlowRabbit;

    private final UpdateRepository updateFlowRabbit;
    private final RabbitMqBaseProperties rabbitMqBaseProperties;

    public static final Gson GSON = new Gson();
    private final AsyncLogsCloudWatchUseCase asyncLogsCloudWatchUseCase;
    public RabbitToCognito(SignUpRepository signUpFlowRabbit,
                           SignInRepository signInFlowRabbit,
                           UpdateRepository updateFlowRabbit,
                           RabbitMqBaseProperties rabbitMqBaseProperties,
                           AsyncLogsCloudWatchUseCase asyncLogsCloudWatchUseCase
    ){
        this.signUpFlowRabbit = signUpFlowRabbit;
        this.rabbitMqBaseProperties = rabbitMqBaseProperties;
        this.asyncLogsCloudWatchUseCase = asyncLogsCloudWatchUseCase;
        this.signInFlowRabbit = signInFlowRabbit;
        this.updateFlowRabbit = updateFlowRabbit;
    }

    @Override
    public byte[] signUp(String pManagementRequest) {
        byte[] reponse =  Mono.just(pManagementRequest)
                .map(req ->{

                    PasswordManagementRequest passwordManagementRequest = null;
                    passwordManagementRequest = GSON.fromJson(pManagementRequest, PasswordManagementRequest.class);
                    var  response = signUpFlowRabbit.signUp(passwordManagementRequest, passwordManagementRequest.getMessageId(), passwordManagementRequest.getConsumer());

                    return response;
                })
                .retry(rabbitMqBaseProperties.getReintentos())
                .map(resp -> GSON.toJson(resp).getBytes(StandardCharsets.UTF_8))
                .onErrorResume(e->{
                    asyncLogsCloudWatchUseCase.putLogsCloudWatch("CALF_DESENCOLAR_ERROR_SIGNUP:",e.getMessage(), RabbitToCognito.class );
                    return Mono.just(GSON.toJson(ApiResponse.createOnError("NoValid"
                                            , "NoValid"
                                            , Constants.PASSWORD_IS_NOT_VALID))
                            .getBytes(StandardCharsets.UTF_8)
                    );
                })
                .share().block();
        return reponse;

    }

    @Override
    public byte[] signIn(String pManagementRequest) {
        try{
            log.info("7passwordManagementRequest: {}", pManagementRequest);
        byte[] reponse =  Mono.just(pManagementRequest)
                .flatMap(req ->{
                    PasswordManagementRequest passwordManagementRequest = null;
                    passwordManagementRequest = GSON.fromJson(pManagementRequest, PasswordManagementRequest.class);
                    asyncLogsCloudWatchUseCase.putLogsCloudWatch("CALF_DESENCOLAR_INFO_SIGNIN: {}", passwordManagementRequest.getMessageId().concat("-").concat(passwordManagementRequest.getConsumer()), RabbitToCognito.class );
                    return signInFlowRabbit.startSignInFlow(passwordManagementRequest, passwordManagementRequest.getMessageId(), passwordManagementRequest.getConsumer());


                })
                .retry(rabbitMqBaseProperties.getReintentos())
                .map(resp -> GSON.toJson(resp).getBytes(StandardCharsets.UTF_8))
                .onErrorResume(e->{
                    asyncLogsCloudWatchUseCase.putLogsCloudWatch("CALF_DESENCOLAR_ERROR_SIGNIN: {}",e.getMessage(), RabbitToCognito.class );
                    return Mono.just(GSON.toJson(
                                    ApiResponse.createOnError("NoValid"
                                            , "CALF"
                                            , Constants.PASSWORD_IS_NOT_VALID))
                            .getBytes(StandardCharsets.UTF_8)
                    );
                })
                .share().block();
        return reponse;

        }catch(Exception e){
            return  GSON.toJson(
                            ApiResponse.createOnError("NoValid"
                                    , "CALF"
                                    , Constants.PASSWORD_IS_NOT_VALID))
                    .getBytes(StandardCharsets.UTF_8);
        }
    }

    @Override
    public byte[] update(String pManagementRequest) {

        byte[] reponse =  Mono.just(pManagementRequest)
                .map(req ->{
                    PasswordManagementRequest passwordManagementRequest = null;
                    passwordManagementRequest = GSON.fromJson(pManagementRequest, PasswordManagementRequest.class);
                    var  response = updateFlowRabbit.startUpdateFlow(passwordManagementRequest, passwordManagementRequest.getMessageId(), passwordManagementRequest.getConsumer());
                    return response;
                })
                .retry(rabbitMqBaseProperties.getReintentos())
                .map(resp -> GSON.toJson(resp).getBytes(StandardCharsets.UTF_8))
                .onErrorResume(e->{
                    asyncLogsCloudWatchUseCase.putLogsCloudWatch("CALF_DESENCOLAR_ERROR_SIGNIN: {}",e.getMessage(), RabbitToCognito.class );
                    return Mono.just(GSON.toJson(
                                    ApiResponse.createOnError("NoValid"
                                            , "CALF"
                                            , Constants.PASSWORD_IS_NOT_VALID))
                            .getBytes(StandardCharsets.UTF_8)
                    );
                })
                .share().block();
        return reponse;
    }
}
