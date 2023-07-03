package co.com.bancolombia.infrastructure.drivenadapter.restconsumer;

import co.com.bancolombia.AlphanumericCreateMain;
import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.savebackup.SaveBackupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.net.ConnectException;
/*
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = AlphanumericCreateMain.class)
@ActiveProfiles("test")
public class SaveBackupRepositoryAdapterTest {

    @Autowired
    SaveBackupRepository saveBackupRepository;

    @Test
    public void whenCallingBackupMethod_thenTheMethodDoRetryAndFail() throws ConnectException {
        String varMessageId = "testMessage";
        String varConsumer = "testConsumer";
        ApiResponse expectedResult = ApiResponse.createOnError(varMessageId, varConsumer, Constants.FAIL_CALLING_SERVICE_CODE);

        PasswordManagementRequest passwordRequest = PasswordManagementRequest.builder().aid("test").password("hash123").build();
        ApiResponse result = saveBackupRepository.saveBackup(passwordRequest, varMessageId, varConsumer);


        assert result != null;
        Assert.isTrue(result.getErrors()[0].equals(expectedResult.getErrors()[0]), "Save backup retry doesn't fail");
    }
}*/
