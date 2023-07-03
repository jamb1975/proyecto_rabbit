package co.com.bancolombia.sqs.gateway;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISqsRepository {
    void sendOneMsgToQueue(String message);
}
