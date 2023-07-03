package co.com.bancolombia.cognito.signup.usecase.util;

public final class Validations {

    private Validations() {
    }


    public static boolean intIsLessThanOrEqual(final int value1, final int value2) {
        return value1 <= value2;
    }

    public static boolean isEmpty(final String s) {
        return s == null || s.trim().isEmpty();
    }


    public static boolean anyStringIsEmpty(final String... valores) {
        final boolean response = false;
        for (final String obj : valores) {
            if (isEmpty(obj)) {
                return true;
            }
        }
        return response;
    }

    public static boolean isAlphaUpperCase(char c) {
        return ((c >= 'A') && (c <= 'Z'));
    }

    public static boolean isAlphaLowerCase(char c) {
        return ((c >= 'a') && (c <= 'z'));
    }

    public static boolean isNumeric(char c) {
        return (c >= '0') && (c <= '9');
    }

    public static boolean isValidDigit00to00FF(char c) {
        return c <= 0xff;
    }

    public static boolean intIsGreaterThanOrEqual(final int valor1, final int valor2) {
        return valor1 >= valor2;
    }
}
