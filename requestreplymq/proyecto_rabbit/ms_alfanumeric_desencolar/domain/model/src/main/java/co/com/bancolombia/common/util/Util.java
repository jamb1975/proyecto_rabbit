package co.com.bancolombia.common.util;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.common.response.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class Util {

    private Util() {
    }

    public static ResponseEntity<ApiResponse> createResponse(ApiResponse apiResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.MESSAGE_ID_HEADER, apiResponse.getMeta().getMessageId());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        headers.add(HttpHeaders.ACCEPT, "application/json");
        headers.add("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        headers.add("X-Frame-Options", "DENY");
        headers.add("X-Content-Type-Options", "nosniff");

        int status = (apiResponse.getErrors() != null) ?
            apiResponse.getErrors()[0].getStatus() :
            apiResponse.getMessage().getStatus();
        return ResponseEntity.status(status)
            .headers(headers)
            .body(apiResponse);
    }
}
