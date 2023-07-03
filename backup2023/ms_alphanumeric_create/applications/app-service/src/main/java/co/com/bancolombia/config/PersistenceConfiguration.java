package co.com.bancolombia.config;

import co.com.bancolombia.util.AwsUtils;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfiguration {

    @Bean
    public DynamoDBMapper dynamoDBMapper(@Value("${aws.region}") String dynamoRegion,
                                         @Value("${aws.dynamodb.endpoint}") String dynamoUrl) {

        return new DynamoDBMapper(
                AmazonDynamoDBClientBuilder
                        .standard()
                        .withEndpointConfiguration(
                                new AwsClientBuilder.EndpointConfiguration(
                                        dynamoUrl, dynamoRegion))
                        .withCredentials(AwsUtils.getCredentials())
                        .build());
    }

}
