package co.com.bancolombia.utility.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderAsyncClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class AwsClientConfig {


    @Bean
    @Profile({"!local"})
    public CognitoIdentityProviderClient cognitoIdentityProviderClient() {
        return CognitoIdentityProviderClient.builder()
            .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
            .region(Region.US_EAST_1)
            .build();
    }

    @Bean
    @Profile({"local"})
    public CognitoIdentityProviderClient localCognitoIdentityProviderClient() {
        return CognitoIdentityProviderClient.builder()
            .credentialsProvider(DefaultCredentialsProvider.create())
            .region(Region.US_EAST_1)
            .build();
    }

    @Bean
    @Profile({"!local"})
    public CognitoIdentityProviderAsyncClient cognitoIdentityProviderAsyncClient() {
        return CognitoIdentityProviderAsyncClient.builder()
            .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
            .region(Region.US_EAST_1)
            .build();
    }

    @Bean
    @Profile({"local"})
    public CognitoIdentityProviderAsyncClient localCognitoIdentityProviderAsyncClient() {
        return CognitoIdentityProviderAsyncClient.builder()
            .credentialsProvider(DefaultCredentialsProvider.create())
            .region(Region.US_EAST_1)
            .build();
    }


}
