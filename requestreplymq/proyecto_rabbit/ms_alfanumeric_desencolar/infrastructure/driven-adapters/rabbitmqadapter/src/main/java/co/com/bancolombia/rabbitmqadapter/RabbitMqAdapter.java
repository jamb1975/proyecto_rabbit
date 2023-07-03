package co.com.bancolombia.rabbitmqadapter;

import co.com.bancolombia.userregister.model.UserRegister;
import co.com.bancolombia.userregister.gateways.UserRegisterRepository;
import com.google.gson.Gson;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.RpcServer;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;

import static reactor.rabbitmq.BindingSpecification.binding;

@Repository
@Log4j2
public class RabbitMqAdapter implements UserRegisterRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqAdapter.class);

    public static final Gson GSON = new Gson();

    @Autowired
    @Qualifier("rpcServerAuth")
    private CalfRpcServerAuth rpcServerAuth;
    @Autowired
    @Qualifier("rpcServerCreate")
    private CalfRpcServerCreate rpcServerCreate;

    @Autowired
    private Mono<Connection> connectionMono;

    @Override
    public void consume() throws InterruptedException, IOException {
        LOGGER.info("channle->{}, queue->{}", rpcServerCreate.getChannel(), rpcServerCreate.getQueueName());
        LOGGER.info("channle->{}, queue->{}", rpcServerAuth.getChannel(), rpcServerAuth.getQueueName());
        Mono.just("Start thread create")
                .publishOn(Schedulers.newParallel("thread-create"))
                .map(msg -> {
                            LOGGER.info(msg);
                            try {
                                 rpcServerCreate.mainloop();
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
                    try {
                        rpcServerAuth.mainloop();
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
        connectionMono.block().close();
    }

}
