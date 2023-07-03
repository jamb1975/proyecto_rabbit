package co.com.bancolombia.parameters.response;

import co.com.bancolombia.common.response.Message;
import co.com.bancolombia.reponse.Meta;

public class ApiResponseParameters {

    private Meta meta;
    private DataParameter[] data;
    private Message message;
    private Error[] errors;

    public ApiResponseParameters(Meta meta, DataParameter[] data, Message message, Error[] errors) {
        //Default constructor
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public DataParameter[] getData() {
        return data;
    }

    public void setData(DataParameter[] data) {
        this.data = data;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Error[] getErrors() {
        return errors;
    }

    public void setErrors(Error[] errors) {
        this.errors = errors;
    }
}

