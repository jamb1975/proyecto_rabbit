package co.com.bancolombia.rabbitmqadapter;

import co.com.bancolombia.cognito.signin.gateways.SignInRepository;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.cognito.signup.gateways.SignUpRepository;
import co.com.bancolombia.cognito.update.gateways.UpdateRepository;
import co.com.bancolombia.rabbitmqadapter.properties.RabbitMqBaseProperties;
import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerCreate;
import co.com.bancolombia.rpcrabbitmq.gateways.RabbitToCognitoRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class RabbitToCognito implements RabbitToCognitoRepository {

    private final SignUpRepository signUpFlowRabbit;
    private SignInRepository signInFlowRabbit;

    private UpdateRepository updateFlowRabbit;
    private final RabbitMqBaseProperties rabbitMqBaseProperties;

    public static final Gson GSON = new Gson();
    private static final Logger LOGGER = LoggerFactory.getLogger(CalfRpcServerCreate.class);

    public RabbitToCognito(SignUpRepository signUpFlowRabbit,
                           SignInRepository signInFlowRabbit,
                           UpdateRepository updateFlowRabbit,
                           RabbitMqBaseProperties rabbitMqBaseProperties
                          ){
        this.signUpFlowRabbit = signUpFlowRabbit;
        this.rabbitMqBaseProperties = rabbitMqBaseProperties;

    }

    @Override
    public byte[] signUp(String pManagementRequest) {
        byte[] reponse =  Mono.just(pManagementRequest)
                .map(req ->{
                    LOGGER.info("request->{}", pManagementRequest);
                    PasswordManagementRequest passwordManagementRequest = null;
                    passwordManagementRequest = GSON.fromJson(pManagementRequest, PasswordManagementRequest.class);
                    var  response = signUpFlowRabbit.signUp(passwordManagementRequest, passwordManagementRequest.getMessageId(), passwordManagementRequest.getConsumer());
                    LOGGER.info("response->{}", response);
                    return response;
                })
                .timeout(Duration.ofMillis(rabbitMqBaseProperties.getTimewait()))
                .retry(rabbitMqBaseProperties.getReintentos())
                .map(resp -> GSON.toJson(resp).getBytes(StandardCharsets.UTF_8)).share().block();
        return reponse;

    }

    @Override
    public byte[] signIn(String pManagementRequest) {
        byte[] reponse =  Mono.just(pManagementRequest)
                .flatMap(req ->{
                     PasswordManagementRequest passwordManagementRequest = null;
                     passwordManagementRequest = GSON.fromJson(pManagementRequest, PasswordManagementRequest.class);
                     return signInFlowRabbit.startSignInFlow(passwordManagementRequest, passwordManagementRequest.getMessageId(), passwordManagementRequest.getConsumer());

                })
                .timeout(Duration.ofMillis(rabbitMqBaseProperties.getTimewait()))
                .retry(rabbitMqBaseProperties.getReintentos())
                .map(resp -> GSON.toJson(resp).getBytes(StandardCharsets.UTF_8)).share().block();
        return reponse;
    }

    @Override
    public byte[] update(String pManagementRequest) {

        byte[] reponse =  Mono.just(pManagementRequest)
                .map(req ->{
                     PasswordManagementRequest passwordManagementRequest = null;
                    passwordManagementRequest = GSON.fromJson(pManagementRequest, PasswordManagementRequest.class);
                    var  response = updateFlowRabbit.startUpdateFlow(passwordManagementRequest, passwordManagementRequest.getMessageId(), passwordManagementRequest.getConsumer());
                    LOGGER.info("response->{}", response);
                    return response;
                })
                .timeout(Duration.ofMillis(rabbitMqBaseProperties.getTimewait()))
                .retry(rabbitMqBaseProperties.getReintentos())
                .map(resp -> GSON.toJson(resp).getBytes(StandardCharsets.UTF_8)).share().block();
        return reponse;
    }
}
