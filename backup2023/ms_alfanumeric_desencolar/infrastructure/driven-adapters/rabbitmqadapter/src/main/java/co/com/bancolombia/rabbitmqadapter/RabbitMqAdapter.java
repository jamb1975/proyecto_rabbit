package co.com.bancolombia.rabbitmqadapter;

import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerAuth;
import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerCreate;
import co.com.bancolombia.rabbitmqadapter.rpcserver.CalfRpcServerUpdate;
import co.com.bancolombia.userregister.gateways.UserRegisterRepository;
import co.com.bancolombia.userregister.model.UserRegister;
import com.google.gson.Gson;
import com.rabbitmq.client.*;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RabbitMqAdapter implements UserRegisterRepository {

    private final CalfRpcServerCreate calfRpcServerCreate;
    private final CalfRpcServerAuth calfRpcServerAuth;
    private final CalfRpcServerUpdate calfRpcServerUpdate;
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqAdapter.class);

    public static final Gson GSON = new Gson();

    public RabbitMqAdapter(CalfRpcServerCreate calfRpcServerCreate,
                           CalfRpcServerAuth calfRpcServerAuth,
                           CalfRpcServerUpdate calfRpcServerUpdate){
        this.calfRpcServerCreate = calfRpcServerCreate;
        this.calfRpcServerAuth = calfRpcServerAuth;
        this.calfRpcServerUpdate = calfRpcServerUpdate;
    }

    @Autowired
    private Mono<Connection> connectionMono;

    @Override
    public void consume() throws InterruptedException, IOException {
        Mono.just("Start thread create")
                .publishOn(Schedulers.newParallel("thread-create"))
                .map(msg -> {
                    LOGGER.info(msg);
                    LOGGER.info(calfRpcServerAuth.getQueueName());
                    try {
                        calfRpcServerCreate.mainloop();

                        return msg;

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .subscribe();

        Mono.just("Start thread auth")
                .publishOn(Schedulers.newParallel("thread-auth"))
                .map(msg -> {
                    LOGGER.info(msg);
                    LOGGER.info(calfRpcServerAuth.getQueueName());
                    try {
                        calfRpcServerAuth.mainloop();
                        return msg;

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .subscribe();

        Mono.just("Start thread update")
                .publishOn(Schedulers.newParallel("thread-update"))
                .map(msg -> {
                    LOGGER.info(msg);
                    LOGGER.info(calfRpcServerUpdate.getQueueName());
                    try {
                        calfRpcServerUpdate.mainloop();
                        return msg;

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .subscribe();


    }

    public Mono<Boolean> checkCognito() {
        return Mono.just(Boolean.TRUE);
    }

    @Override
    public Mono<List<UserRegister>> getReadMq() {
        return Mono.just(new ArrayList<UserRegister>());
    }

    @PreDestroy
    public void close() throws Exception {
        calfRpcServerAuth.terminateMainloop();
        calfRpcServerAuth.close();
        calfRpcServerCreate.terminateMainloop();
        calfRpcServerCreate.close();
        calfRpcServerUpdate.terminateMainloop();
        calfRpcServerUpdate.close();
        connectionMono.block().close();

    }

    private static class TestRpcServer extends RpcServer {

        public TestRpcServer(Channel channel, String queueName) throws IOException {
            super(channel, queueName);
        }
       @Override
        public byte[] handleCall(Delivery request, AMQP.BasicProperties replyProperties) {
            String input = new String(request.getBody());
            LOGGER.info("Recibido from desencolar->{}", input);

            return ("*** " + input + " ***").getBytes();
        }
    }

}
