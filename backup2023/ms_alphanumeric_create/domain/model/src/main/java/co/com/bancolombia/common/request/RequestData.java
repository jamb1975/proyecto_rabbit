package co.com.bancolombia.common.request;

public class RequestData<T> {

    private T[] data;

    public RequestData() {
        // TODO document why this constructor is empty
    }

    public T[] getData() {
        return data;
    }

    public void setData(T[] data) {
        this.data = data;
    }
}
