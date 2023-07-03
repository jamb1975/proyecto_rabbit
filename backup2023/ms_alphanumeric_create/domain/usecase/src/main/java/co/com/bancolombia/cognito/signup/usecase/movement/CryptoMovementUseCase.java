package co.com.bancolombia.cognito.signup.usecase.movement;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.common.response.ApiResponse;
import co.com.bancolombia.common.response.Data;
import co.com.bancolombia.movement.CryptoMovementEntity;
import co.com.bancolombia.movement.gateway.CryptoMovementRepository;
import co.com.bancolombia.parameters.gateways.ParameterRepository;

import java.util.Map;

public class CryptoMovementUseCase {

    public static final int MAX_MOVEMENT_IN_BD = 5;
    private final CryptoMovementRepository movementRepository;
    private final ParameterRepository paramsRepository;

    public CryptoMovementUseCase(CryptoMovementRepository movementRepository,
                                 ParameterRepository paramsRepository) {
        this.movementRepository = movementRepository;
        this.paramsRepository = paramsRepository;
    }

    public ApiResponse createCryptoMovement(CryptoMovementEntity request, String consumer) {

        Map<String, String> params = paramsRepository.mapParameters(consumer);

        int maxMovementInBd = Integer.parseInt(params.get(Constants.MAX_MOVEMENT_IN_BD));

        if (getQuantityMovementByManagementId(request.getCryptoManagementId())
                >= maxMovementInBd) {
            deleteMovement(getMovementByManagementId(request.getCryptoManagementId()));
        }
        movementRepository.save(request);

        return ApiResponse.createOnSuccess(Constants.MESSAGE_ID_HEADER,
                        Constants.CONSUMER_ACRONYM_HEADER, 1).
                setMessage("PathConstants.CREATE_DELEGATE_USER", Constants.INSERT_SUCCESS_DETAIL,
                        Constants.INSERT_SUCCESS_STATUS).setData(new Data[0]);
    }

    public int getQuantityMovementByManagementId(String idManagement) {
        return movementRepository.getQuantityMovementByManagementId(idManagement);
    }

    public CryptoMovementEntity getMovementByManagementId(String idManagement) {
        return movementRepository.getMovementByManagementId(idManagement);
    }

    public void deleteMovement(CryptoMovementEntity movementEntity) {
        movementRepository.delete(movementEntity);
    }


}
