package co.com.bancolombia.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer /* implements Gateway from domain */{

    private final WebClient client;
    //@Value("adapter.restconsumer.path")
    private String path;

    // these methods are an example that illustrates the implementation of WebClient.
    // You should use the methods that you implement from the Gateway from the domain.

    public Mono<ObjectResponse> testGet() {

        return client
            .get()
            .retrieve()
            .bodyToMono(ObjectResponse.class);

    }

    public Mono<ObjectResponse> testPost() {

        ObjectRequest request = ObjectRequest.builder()
            .val1("exampleval1")
            .val2("exampleval2")
            .build();

        return client
            .post()
            .body(Mono.just(request), ObjectRequest.class)
            .retrieve()
            .bodyToMono(ObjectResponse.class);
    }
   /* public Mono<PasswordManagementRequest> savaBackup(PasswordManagementRequest passwordManagementRequest){
        client.post()
                .uri(uriBuilder -> uriBuilder.path(path).build())
                .bodyValue(passwordManagementRequest)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(co.com.bancolombia.common.response.ApiResponse.class)


    }*/
}