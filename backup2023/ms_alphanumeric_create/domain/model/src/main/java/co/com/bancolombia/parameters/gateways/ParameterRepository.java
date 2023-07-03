package co.com.bancolombia.parameters.gateways;

import co.com.bancolombia.parameters.ParametersEntity;

import java.util.List;
import java.util.Map;

public interface ParameterRepository {

    Map<String, String> mapParameters(String consumer);

    List<ParametersEntity> getAllMapParameters();
}
