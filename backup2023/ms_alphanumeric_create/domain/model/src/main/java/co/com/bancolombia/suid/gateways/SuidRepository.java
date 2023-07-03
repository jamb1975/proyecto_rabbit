package co.com.bancolombia.suid.gateways;

import co.com.bancolombia.exeption.AppException;
import co.com.bancolombia.suid.request.GetAliasRequest;
import co.com.bancolombia.suid.request.GetIdentityRequest;
import co.com.bancolombia.suid.response.ApiResponseGetAlias;
import co.com.bancolombia.suid.response.ApiResponseIdentity;

public interface SuidRepository {

    ApiResponseGetAlias getAlias(GetAliasRequest getAliasRequest) throws AppException;

    ApiResponseIdentity getIdentity(GetIdentityRequest getIdentityRequest);
}
