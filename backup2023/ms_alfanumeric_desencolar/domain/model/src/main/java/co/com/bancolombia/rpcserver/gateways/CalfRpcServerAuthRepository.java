package co.com.bancolombia.rpcserver.gateways;

public interface CalfRpcServerAuthRepository {

    public byte[] signUp(String pManagementRequest);
    public void initMainLoop();
    public String getQueueName();
}
