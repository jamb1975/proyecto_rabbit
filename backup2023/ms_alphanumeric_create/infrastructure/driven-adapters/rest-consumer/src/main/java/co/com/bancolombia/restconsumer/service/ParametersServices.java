package co.com.bancolombia.restconsumer.service;

import co.com.bancolombia.parameters.response.ApiResponseParameters;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Url;

import java.util.Map;

public interface ParametersServices {

    @GET
    Call<ApiResponseParameters> getParameters(@Url String url,
                                              @HeaderMap Map<String, String> headers);
}
