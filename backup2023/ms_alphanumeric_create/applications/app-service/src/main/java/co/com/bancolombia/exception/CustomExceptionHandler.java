package co.com.bancolombia.exception;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.common.response.ApiResponse;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class CustomExceptionHandler extends AbstractErrorWebExceptionHandler {

    public CustomExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources,
                                  ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageReaders(configurer.getReaders());
        this.setMessageWriters(configurer.getWriters());
    }
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(),this::renderException);
    }

    private Mono<ServerResponse> renderException(ServerRequest request) {
        Map<String, Object> error = this.getErrorAttributes(request, ErrorAttributeOptions.defaults());

        String messageId = request.headers().firstHeader(Constants.MESSAGE_ID_HEADER.toLowerCase());
        String consumerAcronym = request.headers().firstHeader(Constants.CONSUMER_ACRONYM_HEADER.toLowerCase());
        String errorCode = (String) error.get("errorCode");

        ApiResponse apiResponse =
                ApiResponse.createOnError(messageId, consumerAcronym, errorCode);

        return ServerResponse.status(HttpStatus.resolve((int)error.get("status"))).contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(apiResponse));

    }
}