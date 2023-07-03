package co.com.bancolombia.restconsumer;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.exeption.RetryOnAPIFailureException;
import co.com.bancolombia.restconsumer.util.RetrofitBuilder;
import co.com.bancolombia.restconsumer.util.Util;
import co.com.bancolombia.savebackup.SaveBackupRepository;
import co.com.bancolombia.secret.ServicesCredentials;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import retrofit2.Call;

@Slf4j
@Service
public class SaveBackupRepositoryAdapter implements SaveBackupRepository {

    private final Map<String, String> headers;
    private final String basicInfoPath;
    private final SaveBackupServices saveBackupServices;

    @Autowired
    public SaveBackupRepositoryAdapter(Environment environment,
        ServicesCredentials servicesCredentials) {
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");

        String logsBaseUrl = environment.getProperty("app.backup.host");
        basicInfoPath = environment.getProperty("app.backup.save");

        this.saveBackupServices = RetrofitBuilder.getInstance(logsBaseUrl, servicesCredentials)
            .create(SaveBackupServices.class);
    }

    @Override
    public void saveBackup(PasswordManagementRequest passwordManagementRequest, String messageId, String consumer) throws RetryOnAPIFailureException {
        headers.put(Constants.MESSAGE_ID_HEADER, messageId);
        headers.put(Constants.CONSUMER_ACRONYM_HEADER, consumer);

        Call<ApiResponse> saveInfo = saveBackupServices.saveBackup(
            basicInfoPath, headers, passwordManagementRequest);

        Util.processCall(saveInfo, ApiResponse.class);
    }


}
