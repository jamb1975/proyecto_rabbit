package co.com.bancolombia.cognito.signup.usecase.passwordbusinessvalidator;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.cognito.signup.usecase.util.Validations;

import java.util.regex.Pattern;

public class PasswordBusinessValidator {
    static String regexp = "([A-Za-z0-9]+(-[A-Za-z0-9]+)+)";

    /**
     * Valida si los campos de la peticion no estan vacios o null
     *
     * @param request   peticion con todos los campos
     * @param messageId identificador de la peticion
     * @param consumer  el consumidor que hace la solicitud
     * @return String con el codigo del error en caso que alguno de los parametros este vacio o null
     * , retorna null si los parametros estan completos
     */
    public String requiredFieldIsNullOrEmpty(PasswordManagementRequest request,
                                             String messageId, String consumer) {
        if (Validations.anyStringIsEmpty(request.getAid(), request.getPassword(), messageId,
                consumer)) {
            return Constants.MISSING_BODY_CODE;
        } else {
            return null;
        }
    }

    /**
     * Validar los caracteres de una contraseña return boolean
     *
     * @param password La Clave (Contraseña) para validar
     * @return true si la clave cumple con los criterios 1. La contraseña puede tener cualquier
     * carácter entre 0X00 y 0XFF en al asignación de la clave 2. La contraseña contenga como mínimo
     * 3 de los siguientes 4 tipos de caracteres una letra minúscula, una letra mayúscula, un dígito
     * o un carácter especial. retorna false si algunos de los criterios no se cumple
     */
    public boolean isValidPassword(final String password) {
        var hasLowerCase = 0;
        var hasUppercase = 0;
        var hasNumber = 0;
        var hasSpecialChar = 0;
        for (int i = 0; i < password.length(); i++) {
            var currentChar = password.charAt(i);
            if (Validations.isAlphaUpperCase(currentChar)) {
                hasUppercase = 1;
            } else if (Validations.isAlphaLowerCase(currentChar)) {
                hasLowerCase = 1;
            } else if (Validations.isNumeric(currentChar)) {
                hasNumber = 1;
            } else if (Validations.isValidDigit00to00FF(currentChar)) {
                hasSpecialChar = 1;
            } else {
                hasLowerCase = hasUppercase = hasNumber = hasSpecialChar = 0;
                break;
            }
        }
        return hasLowerCase + hasUppercase + hasNumber + hasSpecialChar >= Constants.THREE;
    }

    public boolean isAlphanumeric(String s) {
        return Pattern.matches(regexp, s);
    }
}
