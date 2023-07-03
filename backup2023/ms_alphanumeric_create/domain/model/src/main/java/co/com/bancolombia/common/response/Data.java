package co.com.bancolombia.common.response;

public class Data<T> {

    public final String type;
    public final String id;
    public final T attributes;

    public Data(String type, String id, T attributes) {
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

    public T getAttributes() {
        return attributes;
    }
}
