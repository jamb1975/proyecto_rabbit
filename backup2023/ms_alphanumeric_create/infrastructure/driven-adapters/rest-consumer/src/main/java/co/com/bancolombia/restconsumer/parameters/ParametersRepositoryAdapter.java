package co.com.bancolombia.restconsumer.parameters;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.exeption.BusinessException;
import co.com.bancolombia.parameters.ParametersEntity;
import co.com.bancolombia.parameters.gateways.ParameterRepository;
import co.com.bancolombia.parameters.response.ApiResponseParameters;
import co.com.bancolombia.restconsumer.service.ParametersServices;
import co.com.bancolombia.restconsumer.util.RequestCommons;
import co.com.bancolombia.restconsumer.util.RetrofitBuilder;
import co.com.bancolombia.secret.ServicesCredentials;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Repository
public class ParametersRepositoryAdapter implements ParameterRepository {

    public static final int TIME_TO_LOAD_PARAMETERS = 1200000;
    private final String getAllParametersPath;
    private final Map<String, String> headers;
    private final ParametersServices parametersServices;
    private final String baseUrl;
    private Map<String, String> mapFinal;
    private List<ParametersEntity> list;


    public ParametersRepositoryAdapter(final Environment environment,
                                       ServicesCredentials servicesCredentials) {
        headers = new HashMap<>();
        headers.put("message-id", UUID.randomUUID().toString());
        headers.put("Consumer-Acronym", Constants.APP_CODE);
        headers.put("CLIENT_ID", servicesCredentials.getApiConnect().getClientId());
        headers.put("CLIENT_SECRET", servicesCredentials.getApiConnect().getClientSecret());
        this.baseUrl = environment.getProperty("app.params.host");
        this.getAllParametersPath = environment.getProperty("app.params.getAllParametersPath");
        this.parametersServices = RetrofitBuilder.getInstance(baseUrl, servicesCredentials)
                .create(ParametersServices.class);
        list = this.getAllMapParameters();
        mapFinal = new HashMap<>();
    }

    @Override
    public Map<String, String> mapParameters(String consumer) {
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
            list = this.getAllMapParameters();
        }
        for (ParametersEntity parametersEntity : list) {
            if (parametersEntity.getConsumer().equals(consumer)) {
                mapFinal.put(parametersEntity.getParameterName(),
                        parametersEntity.getParameterValue());
            }
        }
        if (mapFinal.size() == 0) {
            throw new BusinessException(Constants.CONSUMER_NOT_ALLOWED);
        }
        return mapFinal;
    }

    @Override
    @Cacheable(cacheNames = "allParameters")
    public List<ParametersEntity> getAllMapParameters() {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(
                baseUrl + getAllParametersPath).toUriString();
        var callGetParameters = parametersServices.getParameters(
                urlTemplate, headers);
        var apiResponse =
                RequestCommons.processCall(callGetParameters, ApiResponseParameters.class);
        if (apiResponse != null) {
            list = List.of(apiResponse.getData()[0].getAttributes());
        }
        return list;
    }

    @Scheduled(fixedRate = TIME_TO_LOAD_PARAMETERS)
    public void loadParameters() {
        this.getAllMapParameters();
    }
}

