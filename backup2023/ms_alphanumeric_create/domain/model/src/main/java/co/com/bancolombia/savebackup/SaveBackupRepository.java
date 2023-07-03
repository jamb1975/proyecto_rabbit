package co.com.bancolombia.savebackup;

import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import org.springframework.stereotype.Service;

@Service
public interface SaveBackupRepository {
    void saveBackup(PasswordManagementRequest passwordManagementRequest, String messageId, String consumer);

}
