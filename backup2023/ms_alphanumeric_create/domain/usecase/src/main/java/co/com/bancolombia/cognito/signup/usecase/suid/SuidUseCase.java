package co.com.bancolombia.cognito.signup.usecase.suid;


import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.exeption.BusinessException;
import co.com.bancolombia.suid.gateways.SuidRepository;
import co.com.bancolombia.suid.request.DataGetAliasRequest;
import co.com.bancolombia.suid.request.GetAliasRequest;
import co.com.bancolombia.suid.response.ApiResponseGetAlias;
import co.com.bancolombia.suid.response.AttributesGetAlias;

public class SuidUseCase {

    private final SuidRepository suidRepository;

    public SuidUseCase(SuidRepository suidRepository) {
        this.suidRepository = suidRepository;
    }

    public AttributesGetAlias getAliasByAidConsumer(String aid, String consumer) {
        var dataGetAliasRequests = new DataGetAliasRequest[1];
        dataGetAliasRequests[0] =
                DataGetAliasRequest.builder()
                        .aid(aid)
                        .canal(consumer)
                        .build();
        var suidRequest = new GetAliasRequest(dataGetAliasRequests);

        ApiResponseGetAlias response = suidRepository.getAlias(suidRequest);
        if (response == null) {
            throw new BusinessException(
                    Constants.FAIL_CALLING_SERVICE_CODE);
        } else if (response.getErrors() != null) {
            throw new BusinessException("response.getErrors()[0].getCode()");
        } else if (
                (response.getData().length > 0 && !response.getData()[0].getAttributes().getCanal()
                        .equalsIgnoreCase(consumer)) ||
                        (response.getData().length > 0 && response.getData()[0].getAttributes()
                                .getEstadoAlias().equals(Constants.SUID_REVOKE_STATUS))) {
            return null;
        }
        return
                response.getData()[0].getAttributes();
    }


}
