package co.com.bancolombia.rpcrabbitmq.gateways;

public interface RabbitToCognitoRepository {

    public byte[] signUp(String pManagementRequest);
    public byte[] signIn(String pManagementRequest);
    public byte[] update(String pManagementRequest);
}
