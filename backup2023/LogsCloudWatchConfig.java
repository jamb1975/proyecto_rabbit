/*package co.com.bancolombia.config;

import com.amazonaws.auth.BasicSessionCredentials;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.cloudwatch.model.CloudWatchException;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.DescribeLogStreamsRequest;
import software.amazon.awssdk.services.cloudwatchlogs.model.DescribeLogStreamsResponse;
import software.amazon.awssdk.services.cloudwatchlogs.model.InputLogEvent;
import software.amazon.awssdk.services.cloudwatchlogs.model.PutLogEventsRequest;
import java.util.Arrays;

@Configuration
@ConfigurationProperties(prefix = "cloudwatch")
@Data
@Log4j2
public class LogsCloudWatchConfig {

    private  String logGroup;
    private  String logStreams;

    @Bean
    @Order(1)
    public CloudWatchLogsClient logsClient(){

        CloudWatchLogsClient logsClient = CloudWatchLogsClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
         return logsClient;
    }

    @Bean
    @Order(2)
    public DescribeLogStreamsRequest describeLogStreamsRequest(){
        DescribeLogStreamsRequest logStreamRequest = DescribeLogStreamsRequest.builder()
                .logGroupName(logGroup)
                .logStreamNamePrefix(logStreams)
                .build();
        return logStreamRequest;
    }
    @Bean
    public DescribeLogStreamsResponse describeLogStreamsResponse(){
        DescribeLogStreamsResponse describeLogStreamsResponse = logsClient().describeLogStreams(describeLogStreamsRequest());
        return describeLogStreamsResponse;
    }

    public  void putCWLogEvents(String messages) {
        Mono.just(Tuples.of(messages, logGroup, logStreams, logsClient()))
                .publishOn(Schedulers.newParallel("thread-cloudwatch-logs"))
                .map(t4MsglGrouplStreamslClient -> {
                            try {

                                    String sequenceToken = describeLogStreamsResponse().logStreams().get(0).uploadSequenceToken();

                                   InputLogEvent inputLogEvent = InputLogEvent.builder()
                                            .message(t4MsglGrouplStreamslClient.getT1())
                                            .timestamp(System.currentTimeMillis())
                                            .build();

                                    PutLogEventsRequest putLogEventsRequest = PutLogEventsRequest.builder()
                                        .logEvents(Arrays.asList(inputLogEvent))
                                        .logGroupName(t4MsglGrouplStreamslClient.getT2())
                                        .logStreamName(t4MsglGrouplStreamslClient.getT3())
                                        .sequenceToken(sequenceToken)
                                        .build();

                                    t4MsglGrouplStreamslClient.getT4().putLogEvents(putLogEventsRequest);
                                    log.info("Successfully put CloudWatch log event");
                                    return true;

                            } catch (CloudWatchException e) {
                               log.info(e.awsErrorDetails().errorMessage());
                               return false;

                            }
                        }
                ).subscribe();
    }
}
*/