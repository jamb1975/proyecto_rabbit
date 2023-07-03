package co.com.bancolombia.common.response;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.reponse.Meta;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class ApiResponse {

    private Meta meta;
    private Data<?>[] data;
    private Message message;
    private Error[] errors;

    private ApiResponse() {
    }

    public static ApiResponse createOnError(String messageId, String consumerAcronym,
        String errorCode) {
        return new ApiResponse()
            .setMeta(messageId, Constants.ONE, consumerAcronym)
            .setErrors(errorCode);
    }

    public static ApiResponse createOnError(String messageId, String consumerAcronym,
        String errorCode, String detail) {
        return new ApiResponse()
            .setMeta(messageId, Constants.ONE, consumerAcronym)
            .setErrors(generateErrors(errorCode, detail));
    }


    public static ApiResponse createOnSuccess(String messageId, String consumerAcronym,
        int responseSize) {
        return new ApiResponse()
            .setMeta(messageId, responseSize, consumerAcronym);
    }

    private static Error[] generateErrors(String errorCode, String detail) {
        Error[] errors = new Error[Constants.ONE];
        errors[0] = new Error(Constants.VALIDATION_ERROR_STATUS, errorCode, detail);
        return errors;
    }

    private ApiResponse setMeta(String messageId, int responseSize, String clientRequest) {
        LocalDateTime date = LocalDateTime.now(ZoneOffset.of("-05:00"));
        String dateString = date.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
        this.meta = new Meta(messageId, dateString, responseSize, clientRequest);
        return this;
    }

    public ApiResponse setData(String type, Object result, String id) {
        Data<?>[] arrayData = null;
        if (result != null) {
            arrayData = new Data[Constants.ONE];
            arrayData[0] = new Data<>(type, id, result);
        }
        this.data = arrayData;
        return this;
    }

    public Meta getMeta() {
        return meta;
    }

    public Data[] getData() {
        return data;
    }

    public ApiResponse setData(Data<?>[] data) {
        this.data = data;
        return this;
    }

    public Message getMessage() {
        return message;
    }

    public Error[] getErrors() {
        return errors;
    }

    private ApiResponse setErrors(String code) {
        Error error = Constants.getErrorMessage(code);
        this.errors = new Error[]{error};
        return this;
    }

    private ApiResponse setErrors(Error[] errors) {
        this.errors = errors;
        return this;
    }

    public ApiResponse setMessage(String source, String detail, int status) {
        this.message = new Message(source, detail, Constants.SUCCESS_TRANSACTION_TYPE,
            Constants.SUCCESS_TRANSACTION_TITLE, status);
        return this;
    }
}
