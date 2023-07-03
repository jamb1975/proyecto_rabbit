package co.com.bancolombia.rpcserver.gateways;

public interface CalfRpcServerAuthRepository {

    public byte[] signIn(String pManagementRequest);

}
