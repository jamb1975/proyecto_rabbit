package co.com.bancolombia.rpcserver.gateways;

public interface CalfRpcServerUpdateRepository {

    public byte[] update(String pManagementRequest);
    public void initMainLoop();
    public String getQueueName();
}
