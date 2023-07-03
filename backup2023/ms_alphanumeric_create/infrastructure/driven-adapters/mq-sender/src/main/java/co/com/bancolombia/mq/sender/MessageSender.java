package co.com.bancolombia.mq.sender;

import co.com.bancolombia.SenderQueueAdapter;
import co.com.bancolombia.cognito.signin.model.PasswordManagementRequest;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import org.springframework.stereotype.Service;

@Service
public class MessageSender implements SenderQueueAdapter {

    public static final String QUEUE_NAME = "queue-calf";


    @Override
    public void sendQueue(PasswordManagementRequest passwordManagementRequest) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (
            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            channel.basicPublish("", QUEUE_NAME, null, passwordManagementRequest.toString().getBytes(StandardCharsets.UTF_8));
            System.out.println(" Enviando mensaje.... " + passwordManagementRequest.toString());


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

    }
}
