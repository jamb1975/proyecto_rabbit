package co.com.bancolombia.common.util;

import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;

public interface HashPassToCognitoAdapter {

    PasswordManagementRequest hashPass(PasswordManagementRequest passwordManagementRequest);

}
