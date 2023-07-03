package co.com.bancolombia.utility;

import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import co.com.bancolombia.common.util.HashPassToCognitoAdapter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class HashAdapter implements HashPassToCognitoAdapter {

    @Override
    public PasswordManagementRequest hashPass(PasswordManagementRequest passwordManagementRequest) {
        var password = passwordManagementRequest.getAid() + passwordManagementRequest.getPassword();
        var passwordHash = DigestUtils.sha256Hex(password);
        passwordManagementRequest.setPassword(passwordHash);
        return passwordManagementRequest;
    }
}