package co.com.bancolombia.sqs.sender;

import co.com.bancolombia.sqs.gateway.ISqsRepository;
import co.com.bancolombia.sqs.sender.config.SQSSenderProperties;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@AllArgsConstructor
public class SQSSender implements ISqsRepository {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;


    public void sendOneMsgToQueue(String message){
        Mono.fromCallable(()->"Call processs qs")
                .publishOn(Schedulers.newParallel("Thread Sqs"))
                .map(msg ->{
                    try{
                        SendMessageResponse response = client.sendMessage(buildRequest(message)).get();
                        log.info("Message sent {}", response.messageId());

                      } catch (Exception e) {
                        log.error("Se presento un error al escribir el mensaje de reintento. ", e);
                    }
                return msg;
                }).subscribe();

        log.info("Message sent {}", message);


    }

    private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.getQueueUrl())
                .messageBody(message)
                .build();
    }
}
