package co.com.bancolombia.parameters.response;

import co.com.bancolombia.parameters.ParametersEntity;

public class DataParameter {

    public final String type;
    public final String id;
    public final ParametersEntity[] attributes;

    public DataParameter(String type, String id, ParametersEntity[] attributes) {
        this.type = type;
        this.id = id;
        this.attributes = attributes;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public ParametersEntity[] getAttributes() {
        return attributes;
    }
}

