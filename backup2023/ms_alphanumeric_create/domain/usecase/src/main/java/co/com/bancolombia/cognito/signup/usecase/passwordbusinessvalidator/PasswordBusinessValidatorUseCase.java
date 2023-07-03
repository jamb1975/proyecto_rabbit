package co.com.bancolombia.cognito.signup.usecase.passwordbusinessvalidator;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.cognito.signup.usecase.util.Validations;
import co.com.bancolombia.exeption.BusinessException;
import co.com.bancolombia.parameters.gateways.ParameterRepository;
import co.com.bancolombia.passwordvalidatormodel.PasswordValidatorModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class PasswordBusinessValidatorUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            PasswordBusinessValidatorUseCase.class);
    private static final int MIN_NUM_CHARS_PASSWORD = Constants.EIGHT;
    private static final int MAX_NUM_CHARS_PASSWORD = Constants.SIXTY_FOUR;
    private final ParameterRepository paramsRepository;
    private final PasswordBusinessValidator passwordBusinessValidator;

    public boolean validatePassword(final PasswordValidatorModel passwordValidatorModel) {
        return Stream.of(validateConsumerParams(passwordValidatorModel),
                validateCharacterRules(passwordValidatorModel)).allMatch(a -> a);
    }

    public boolean validateConsumerParams(final PasswordValidatorModel passwordValidatorModel) {
        Map<String, String> params = paramsRepository.mapParameters(
                passwordValidatorModel.getConsumer());
        //Se valida la cantidad minima y maxima de caracteres
        return validatePasswordLength(passwordValidatorModel, params);
    }

    private boolean validatePasswordLength(final PasswordValidatorModel passwordManagementRequest,
                                           final Map<String, String> params) {
        int minNumCharsPassword = Integer.parseInt(params.get(Constants.MIN_NUM_CHARS_PASSWORD));
        if (!Validations.intIsGreaterThanOrEqual(passwordManagementRequest.getPassword().length(),
                minNumCharsPassword) && minNumCharsPassword >= MIN_NUM_CHARS_PASSWORD) {
            return false;
        }
        int maxNumCharsPassword = Integer.parseInt(params.get(Constants.MAX_NUM_CHARS_PASSWORD));
        return Validations.intIsLessThanOrEqual(passwordManagementRequest.getPassword().length(),
                maxNumCharsPassword) ||
                maxNumCharsPassword < MAX_NUM_CHARS_PASSWORD;
    }

    public boolean validateCharacterRules(final PasswordValidatorModel passwordManagementRequest) {
        return passwordBusinessValidator.isValidPassword(passwordManagementRequest.getPassword());
    }

    public boolean validatePasswordTimeStamp(final String timeStampRequest) {
        try {
            var timeStampRequestToEval = OffsetDateTime.parse(timeStampRequest);
            OffsetDateTime now = OffsetDateTime.now(
                    ZoneOffset.of(timeStampRequestToEval.getOffset().getId()));
            var delayMinutes = Constants.FIVE;
            //Sumar tiempo de vigencia de la peticion
            timeStampRequestToEval = (timeStampRequestToEval.plusMinutes(delayMinutes));
            if (now.compareTo(timeStampRequestToEval) > Constants.ZERO) {
                return false;
            }
            var timeStampRequestToEvalUP = OffsetDateTime.parse(timeStampRequest)
                    .plusMinutes(-delayMinutes);
            if (timeStampRequestToEvalUP.compareTo(now) > 0) {
                return false;
            }
        } catch (DateTimeException e) {
            throw new BusinessException(Constants.PASSWORD_DOES_NOT_MEET_ACCEPTANCE_CRITERIA);
        }
        return true;
    }
}
