package co.com.bancolombia.restconsumer;

import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import java.util.Map;

import co.com.bancolombia.common.response.ApiResponse;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;
import retrofit2.Call;

public interface SaveBackupServices {
    @POST
    Call<ApiResponse> saveBackup(@Url String url,
                                 @HeaderMap Map<String, String> headers,
                                 @Body PasswordManagementRequest request);

}
