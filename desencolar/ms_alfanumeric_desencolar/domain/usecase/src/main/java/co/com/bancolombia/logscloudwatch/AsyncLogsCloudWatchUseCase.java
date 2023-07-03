package co.com.bancolombia.logscloudwatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

public class AsyncLogsCloudWatchUseCase {

       public  boolean putLogsCloudWatch(String id, String mesage, Class<?> sourceClass ){

        Mono.just(Tuples.of(id, mesage, sourceClass))
                .publishOn(Schedulers.newParallel("Async With Thread Log Cloudwatch"))
                .map(t3IdMsgSourceClass -> {
                    Logger LOGGER = LoggerFactory.getLogger(t3IdMsgSourceClass.getT3());
                    LOGGER.info(t3IdMsgSourceClass.getT1().concat(": {}"), t3IdMsgSourceClass.getT2());
                    return true;
                })
                .subscribe();
         return true;
        }
    public  boolean putLogsCloudWatch(String id, int tipoInfo, String mesage, Class<?> sourceClass ){

        Mono.just(Tuples.of(id, mesage, sourceClass))
                .publishOn(Schedulers.newParallel("Async With Thread Log Cloudwatch"))
                .map(t3IdMsgSourceClass -> {
                    Logger LOGGER = LoggerFactory.getLogger(t3IdMsgSourceClass.getT3());
                    if(tipoInfo == 0) LOGGER.info(t3IdMsgSourceClass.getT1().concat(": {}"), t3IdMsgSourceClass.getT2());
                        else if(tipoInfo == 1) LOGGER.warn(t3IdMsgSourceClass.getT1().concat(": {}"), t3IdMsgSourceClass.getT2());
                            else  LOGGER.error(t3IdMsgSourceClass.getT1().concat(": {}"), t3IdMsgSourceClass.getT2());
                    return true;
                })
                .subscribe();
        return true;
    }
}
