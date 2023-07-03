package co.com.bancolombia.cognito.signup.usecase.savebackup;

import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.savebackup.SaveBackupRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SaveBackupUseCase {

    private final SaveBackupRepository saveBackupRepository;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public SaveBackupUseCase(SaveBackupRepository saveBackupRepository) {
        this.saveBackupRepository = saveBackupRepository;
    }

    public void saveBackup(PasswordManagementRequest passwordManagementRequest, String messageId, String consumer){
        saveBackupRepository.saveBackup(passwordManagementRequest, messageId, consumer);
    }

}
