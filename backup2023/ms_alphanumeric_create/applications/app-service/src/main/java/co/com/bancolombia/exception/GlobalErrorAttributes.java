package co.com.bancolombia.exception;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.exeption.BusinessException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {


    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorMap = new HashMap<>();
        Throwable error = getError(request);

        if (error instanceof BusinessException){
            errorMap.put("status",  Constants.VALIDATION_ERROR_STATUS);
            errorMap.put("errorCode", error.getMessage());
        }else{
            errorMap.put("status",  Constants.INTERNAL_ERROR_STATUS);
            errorMap.put("errorCode", Constants.UNHANDLED_ERROR_CODE);
        }

        return errorMap;
    }
}
