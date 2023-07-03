package co.com.bancolombia.cognito.signin.model;

import co.com.bancolombia.common.response.Error;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public final class Constants {


    public static final String DATE_FORMAT = "dd/MM/yyyy hh:mm:ss:ms";
    public static final Gson GSON = new Gson();
    public static final String MESSAGE_ID_HEADER = "Message-Id";
    public static final String CONSUMER_ACRONYM_HEADER = "Consumer-Acronym";
    public static final String VALIDATED_PASSWORD = "Clave exitosa";
    public static final String PASSWORD_IS_NOT_VALID = "ER404-07";
    //NUMBERS
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int EIGHT = 8;
    public static final int SIXTY_FOUR = 64;

    //NUMERIC RESPONSE CODES
    public static final int NOT_CONTENT = 204;
    public static final int VALIDATION_ERROR_STATUS = 400;
    public static final int INTERNAL_ERROR_STATUS = 500;
    public static final int INSERT_SUCCESS_STATUS = 201;
    public static final int CREATE_SUCCESS_STATUS = 201;
    public static final int PROCESS_SUCCESS_STATUS = 200;
    public static final int AUTHENTICATION_ERROR_STATUS = 401;

    public static final String INVALID_IDENTIFIER = "ER404-01";
    public static final String PASSWORD_ALREADY_EXISTS = "ER404-06";

    public static final String UNHANDLED_ERROR_CODE = "ER500-01";

    public static final String SUCCESS_TRANSACTION_TYPE = "SUCCESS";

    public static final String SUCCESS_TRANSACTION_TITLE = "Operación exitosa";
    public static final String FAILED_TRANSACTION_TITLE = "Operación fallida";

    public static final String PASSWORD_CREATE_SUCCESS_DETAIL = "Clave creada satisfactoriamente";

    private static final Map<String, Error> errorMessages = new HashMap<>();

    static {

        errorMessages.put(INVALID_IDENTIFIER,
            new Error(VALIDATION_ERROR_STATUS, INVALID_IDENTIFIER,
                "Usuario no confirmado."));
        errorMessages.put(PASSWORD_ALREADY_EXISTS,
            new Error(VALIDATION_ERROR_STATUS, PASSWORD_ALREADY_EXISTS,
                "Usuario con clave asignada."));

        errorMessages.put(UNHANDLED_ERROR_CODE,
            new Error(INTERNAL_ERROR_STATUS, UNHANDLED_ERROR_CODE,
                "Ha ocurrido un error interno en el servidor."));


    }

    private Constants() {
    }

    public static Error getErrorMessage(String key) {
        return errorMessages.get(key);
    }


}
