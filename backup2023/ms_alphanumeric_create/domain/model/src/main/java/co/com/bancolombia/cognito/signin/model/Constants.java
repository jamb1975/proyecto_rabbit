package co.com.bancolombia.cognito.signin.model;

import co.com.bancolombia.common.response.Error;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public final class Constants {

    public static final String DATE_FORMAT = "dd/MM/yyyy hh:mm:ss:ms";
    public static final String DATE_FORMAT_TIME_ZONE = "yyyy-MM-dd HH:mm:ss";
    public static final Gson GSON = new Gson();
    public static final String APP_CODE = "NU01910001";
    public static final String CONSUMER_ACRONYMUS = "CALF";

    public static final int BCRYPT_SALT_LEN = 16;
    public static final int BCRYPT_CRYPTOGRAM_SIZE = 60;

    public static final String PROJECT_ID = "CALF";

    //PARAMETERS NAMES
    public static final String MAX_NUM_CHARS_PASSWORD = "maxNumCharsPassword";
    public static final String MIN_NUM_CHARS_PASSWORD = "minNumCharsPassword";
    public static final String TOKEN_JWT_VALIDITY = "tokenJWTValidity";
    public static final String MAX_PASSWORD_UPDATE_TIME = "maxPasswordUpdateTime";
    public static final String MIN_PASSWORD_UPDATE_TIME = "minPasswordUpdateTime";
    public static final String BLOCKING_TIME_FAILED_ATTEMPTS = "blockingTimeFailedAttempts";
    public static final String MAX_ATTEMPTS_VALIDATION = "maxAttemptsValidation";
    public static final String TIMEOUT_PASSWORD = "timeoutPassword";
    public static final String MAX_MOVEMENT_IN_BD = "maxMovementInBd";


    //Status CODE
    public static final String STATUS_CODE_ACTIVE = "1";
    //STATUS
    public static final String ACTIVE = "ACTIVO";
    public static final String LOCKED_FOR_ATTEMPTS = "BLOQUEADO POR INTENTOS FALLIDOS";

    //NUMERIC RESPONSE CODES
    public static final int NOT_CONTENT = 204;
    public static final int VALIDATION_ERROR_STATUS = 400;
    public static final int INTERNAL_ERROR_STATUS = 500;
    public static final int INSERT_SUCCESS_STATUS = 201;
    public static final int CREATE_SUCCESS_STATUS = 201;
    public static final int PROCESS_SUCCESS_STATUS = 200;
    public static final int AUTHENTICATION_ERROR_STATUS = 401;
    //TRANSACTION RESULTS TITLE
    public static final String SUCCESS_TRANSACTION_TITLE = "Operación exitosa";
    public static final String FAILED_TRANSACTION_TITLE = "Operación fallida";
    public static final String INSERT_SUCCESS_DETAIL = "El registro se ha creado correctamente";
    public static final String PASSWORD_CREATE_SUCCESS_DETAIL = "Clave creada satisfactoriamente";
    public static final String VALIDATED_PASSWORD = "Clave exitosa";
    public static final String FAILED_VALIDATED_PASSWORD = "Clave errada";
    public static final String TIME_OUT = "";

    //PARAMETERS LOGS
    public static final String USER = "USER";
    public static final String SELECT = "SELECT";
    public static final String SYSTEM = "System";
    public static final String BEFORE_VALUE_VALIDATE = "Clave alfanumérica sin validar";
    public static final String AFTER_VALUE_VALIDATE = "Clave alfanumérica validada";
    public static final String APP = "CLAVE";
    public static final String DESCRIPTION_VALIDATE = "Validar clave alfanumérica";

    //TRANSACTION RESULTS TYPE
    public static final String SUCCESS_TRANSACTION_TYPE = "SUCCESS";
    public static final String FAILED_TRANSACTION_TYPE = "FAILED";

    //HEADERS NAMES
    public static final String CONSUMER_ACRONYM_HEADER = "Consumer-Acronym";
    public static final String MESSAGE_ID_HEADER = "Message-Id";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String CLIENT_SECRET_HEADER = "CLIENT_SECRET";
    public static final String CLIENT_ID_HEADER = "CLIENT_ID";
    //NUMBERS
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int EIGHT = 8;
    public static final int SIXTY_FOUR = 64;
    //Special Chart
    public static final String PIPE_LINE = "|";

    //INTERNAL ERROR
    public static final String UNHANDLED_ERROR_CODE = "ER500-01";
    public static final String FAIL_CALLING_SERVICE_CODE = "ER500-02";

    public static final String SUID_REVOKE_STATUS = "REVOCADO";
    public static final String IDENTIFIER_AND_CONSUMER_DOES_NOT_EXIST_IN_OUR_DATABASE = "ER500-03";


    //Business Error
    public static final String MISSING_HEADERS_CODE = "ER404-01";
    public static final String MISSING_BODY_CODE = "ER404-02";
    public static final String CONSUMER_NOT_ALLOWED = "ER404-03";
    public static final String PASSWORD_DOES_NOT_MEET_ACCEPTANCE_CRITERIA = "ER404-04";
    public static final String USER_NOT_AVAILABLE = "ER404-05";
    public static final String PASSWORD_ALREADY_EXISTS = "ER404-06";
    public static final String PASSWORD_IS_NOT_VALID = "ER404-07";
    public static final String CLIENT_NOT_AUTHORIZED = "ER404-08";
    public static final String PASSWORD_IS_EXPIRES = "ER404-09";
    public static final String INVALID_DATA_CODE = "ER404-10";
    public static final String PASSWORD_IS_LOCKED = "ER404-11";
    public static final String MESSAGE_DOES_NOT_CIPHERED_WITH_KMS = "ER404-12";
    public static final String INVALID_IDENTIFIER = "ER404-13";
    //lOGS
    public static final String LOGS_CLASSIFICATION = "USER";
    public static final String APPLICATION_NAME = "Clave alfanumérica";

    private static final Map<String, Error> errorMessages = new HashMap<>();

    static {

        errorMessages.put(UNHANDLED_ERROR_CODE,
                new Error(INTERNAL_ERROR_STATUS, UNHANDLED_ERROR_CODE,
                        "Ha ocurrido un error interno en el servidor."));

        errorMessages.put(FAIL_CALLING_SERVICE_CODE,
                new Error(INTERNAL_ERROR_STATUS, FAIL_CALLING_SERVICE_CODE,
                        "Error llamando a un servicio."));

        errorMessages.put(IDENTIFIER_AND_CONSUMER_DOES_NOT_EXIST_IN_OUR_DATABASE,
                new Error(VALIDATION_ERROR_STATUS, UNHANDLED_ERROR_CODE,
                        "Aid Y Consumidor no existe en nuestra base datos de clave alfanumérica."));

        //Negocio
        errorMessages.put(MISSING_HEADERS_CODE,
                new Error(VALIDATION_ERROR_STATUS, MISSING_HEADERS_CODE,
                        "Mensaje enviado por el sistema consumidor no es valido"));

        errorMessages.put(MISSING_BODY_CODE,
                new Error(VALIDATION_ERROR_STATUS, MISSING_BODY_CODE,
                        "Faltan parámetros obligatorios"));

        errorMessages.put(CONSUMER_NOT_ALLOWED,
                new Error(VALIDATION_ERROR_STATUS, CONSUMER_NOT_ALLOWED,
                        "Consumidor no habilitado."));

        errorMessages.put(PASSWORD_DOES_NOT_MEET_ACCEPTANCE_CRITERIA,
                new Error(VALIDATION_ERROR_STATUS, PASSWORD_DOES_NOT_MEET_ACCEPTANCE_CRITERIA,
                        "Su clave no cumple con los criterios de aceptación."));

        errorMessages.put(USER_NOT_AVAILABLE,
                new Error(VALIDATION_ERROR_STATUS, USER_NOT_AVAILABLE, "Usuario no disponible."));

        errorMessages.put(PASSWORD_ALREADY_EXISTS,
                new Error(VALIDATION_ERROR_STATUS, PASSWORD_ALREADY_EXISTS,
                        "Usuario con clave asignada."));

        errorMessages.put(PASSWORD_IS_NOT_VALID,
                new Error(VALIDATION_ERROR_STATUS, PASSWORD_IS_NOT_VALID, "Clave errada."));

        errorMessages.put(CLIENT_NOT_AUTHORIZED,
                new Error(VALIDATION_ERROR_STATUS, CLIENT_NOT_AUTHORIZED, "Cliente no autorizado."));

        errorMessages.put(PASSWORD_IS_EXPIRES,
                new Error(VALIDATION_ERROR_STATUS, PASSWORD_IS_EXPIRES,
                        "Su clave se encuentra expirada."));

        errorMessages.put(INVALID_DATA_CODE,
                new Error(VALIDATION_ERROR_STATUS, INVALID_DATA_CODE,
                        "Uno o más datos no poseen un valor válido."));

        errorMessages.put(PASSWORD_IS_LOCKED,
                new Error(VALIDATION_ERROR_STATUS, PASSWORD_IS_LOCKED,
                        "Su clave se encuentra bloqueada."));

        errorMessages.put(MESSAGE_DOES_NOT_CIPHERED_WITH_KMS,
                new Error(VALIDATION_ERROR_STATUS, MESSAGE_DOES_NOT_CIPHERED_WITH_KMS,
                        "Mensaje no se encuentra cifrado con KMS."));
        errorMessages.put(INVALID_IDENTIFIER,
                new Error(VALIDATION_ERROR_STATUS, INVALID_IDENTIFIER,
                        "El identificador no es valido."));


    }

    private Constants() {
    }

    public static Error getErrorMessage(String key) {
        return errorMessages.get(key);
    }

}
