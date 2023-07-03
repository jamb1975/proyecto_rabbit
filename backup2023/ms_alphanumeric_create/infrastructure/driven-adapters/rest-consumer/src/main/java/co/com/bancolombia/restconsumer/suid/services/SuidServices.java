package co.com.bancolombia.restconsumer.suid.services;

import co.com.bancolombia.suid.request.GetAliasRequest;
import co.com.bancolombia.suid.request.GetIdentityRequest;
import co.com.bancolombia.suid.response.ApiResponseGetAlias;
import co.com.bancolombia.suid.response.ApiResponseIdentity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

import java.util.Map;

public interface SuidServices {

    @POST
    Call<ApiResponseGetAlias> getAlias(@Url String url,
                                       @HeaderMap Map<String, String> headers,
                                       @Body GetAliasRequest request);

    @POST
    Call<ApiResponseIdentity> getIdentity(@Url String url,
                                          @HeaderMap Map<String, String> headers,
                                          @Body GetIdentityRequest request);
}
