package co.com.bancolombia;

import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;

public interface SenderQueueAdapter {

    void sendQueue(PasswordManagementRequest passwordManagementRequest);

}
