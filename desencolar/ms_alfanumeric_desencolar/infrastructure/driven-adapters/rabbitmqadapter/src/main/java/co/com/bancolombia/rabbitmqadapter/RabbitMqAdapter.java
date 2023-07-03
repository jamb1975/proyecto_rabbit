package co.com.bancolombia.rabbitmqadapter;

import co.com.bancolombia.logscloudwatch.AsyncLogsCloudWatchUseCase;
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

@Log4j2
public class RabbitMqAdapter implements UserRegisterRepository {

    private final CalfRpcServerCreate calfRpcServerCreate;
    private final CalfRpcServerAuth calfRpcServerAuth;
    private final CalfRpcServerUpdate calfRpcServerUpdate;
    private final AsyncLogsCloudWatchUseCase asyncLogsCloudWatchUseCase;

    public RabbitMqAdapter(CalfRpcServerCreate calfRpcServerCreate,
                           CalfRpcServerAuth calfRpcServerAuth,
                           CalfRpcServerUpdate calfRpcServerUpdate,
                           AsyncLogsCloudWatchUseCase asyncLogsCloudWatchUseCase){
        this.calfRpcServerCreate = calfRpcServerCreate;
        this.calfRpcServerAuth = calfRpcServerAuth;
        this.calfRpcServerUpdate = calfRpcServerUpdate;
        this.asyncLogsCloudWatchUseCase = asyncLogsCloudWatchUseCase;
    }

    @Autowired
    private Mono<Connection> connectionMono;

    @Override
    public boolean consume() {

        try {
            Mono.just("Start thread create")
                    .publishOn(Schedulers.newParallel(""))
                    .map(msg -> {
                        asyncLogsCloudWatchUseCase.putLogsCloudWatch("CALF_DESENCOLAR_INFO_CONSUME", msg, RabbitMqAdapter.class);

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
                        asyncLogsCloudWatchUseCase.putLogsCloudWatch("CALF_DESENCOLAR_INFO_CONSUME", msg, RabbitMqAdapter.class);

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
                        asyncLogsCloudWatchUseCase.putLogsCloudWatch("CALF_DESENCOLAR_INFO_CONSUME", msg, RabbitMqAdapter.class);

                        try {
                            calfRpcServerUpdate.mainloop();
                            return msg;

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .subscribe();

            return true;
        }catch (Exception e){

            return false;
        }
    }


}
