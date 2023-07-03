package co.com.bancolombia.cognito.signup;

import co.com.bancolombia.cognito.admin.OpsForUser;
import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.cognito.signup.usecase.signup.gateway.SignUpUseCase;
import co.com.bancolombia.common.util.PathConstants;
import co.com.bancolombia.common.util.Util;
import co.com.bancolombia.common.request.RequestData;
import co.com.bancolombia.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CreateController {

    private final SignUpUseCase signUpUseCase;
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateController.class);

    /**
     * Registro (pre login, pre autenticación)
     *
     * @params body deberia recibir el correo o algun dato para verificación del usuario
     * @return
     */
    @PostMapping(PathConstants.CREATE_PASSWORD)
    public Mono<ResponseEntity<ApiResponse>> startSignUpFlow(
            @RequestHeader(Constants.MESSAGE_ID_HEADER) String messageId,
            @RequestHeader(Constants.CONSUMER_ACRONYM_HEADER) String consumer,
            @RequestBody RequestData<PasswordManagementRequest> request) {
        LOGGER.info("MESSAGE_ID_HEADER->{}",messageId );
        LOGGER.info("CONSUMER_ACRONYM_HEADER->{}", consumer);
       return signUpUseCase.startSignUpFlow(request.getData()[0],
                messageId, consumer).log()
                .map(res -> Util.createResponse(res.getBody()));
    }

    @PostMapping(path = "/_remove")
    public ResponseEntity<String> deleteUser(@RequestBody OpsForUser opsForUser) {
        return ResponseEntity.ok(signUpUseCase.deleteUser(opsForUser));
    }
}
