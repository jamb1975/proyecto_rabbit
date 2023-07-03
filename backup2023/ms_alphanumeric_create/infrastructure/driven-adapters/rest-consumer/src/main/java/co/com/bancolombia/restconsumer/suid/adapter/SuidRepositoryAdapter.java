package co.com.bancolombia.restconsumer.suid.adapter;

import co.com.bancolombia.exeption.AppException;
import co.com.bancolombia.restconsumer.suid.services.SuidServices;
import co.com.bancolombia.restconsumer.util.RequestCommons;
import co.com.bancolombia.restconsumer.util.RetrofitBuilder;
import co.com.bancolombia.secret.ServicesCredentials;
import co.com.bancolombia.suid.gateways.SuidRepository;
import co.com.bancolombia.suid.request.GetAliasRequest;
import co.com.bancolombia.suid.request.GetIdentityRequest;
import co.com.bancolombia.suid.response.ApiResponseGetAlias;
import co.com.bancolombia.suid.response.ApiResponseIdentity;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import retrofit2.Call;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class SuidRepositoryAdapter implements SuidRepository {

    private final Map<String, String> headers;
    private final SuidServices suidServices;
    private final String getAlias;
    private final String getIdentityPath;

    public SuidRepositoryAdapter(Environment environment, ServicesCredentials credentials) {
        headers = new HashMap<>();
        headers.put("message-id", UUID.randomUUID().toString());
        headers.put("content-type", "application/json");
        headers.put("client_id", credentials.getSuidService().getClientId());
        headers.put("client_secret", credentials.getSuidService().getClientSecret());

        String baseUrl = environment.getProperty("app.suid.host");
        this.getIdentityPath = environment.getProperty("app.suid.getIdentityPath");
        this.getAlias = environment.getProperty("app.suid.getAlias");

        this.suidServices = RetrofitBuilder.getInstance(baseUrl, credentials)
                .create(SuidServices.class);
    }

    @Override
    public ApiResponseGetAlias getAlias(GetAliasRequest getAliasRequest) throws AppException {
        this.headers.put("Consumer-Acronym", getAliasRequest.getData()[0].getCanal());
        Call<ApiResponseGetAlias> createRelationCall = suidServices.getAlias(
                getAlias, headers, getAliasRequest);
        return RequestCommons.processGetAliasCall(createRelationCall, ApiResponseGetAlias.class);
    }

    @Override
    public ApiResponseIdentity getIdentity(GetIdentityRequest getIdentityRequest) {

        Call<ApiResponseIdentity> getIdentityCall = suidServices.getIdentity(
                getIdentityPath, headers, getIdentityRequest);
        return RequestCommons.processCall(getIdentityCall, ApiResponseIdentity.class);
    }
}


