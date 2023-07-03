package co.com.bancolombia.kms.config;

import co.com.bancolombia.utility.aws.AwsUtil;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;

@Configuration
@RequiredArgsConstructor
public class KmsConfig {

    private final AwsUtil awsUtil;

    @Bean
    public AWSKMS awsKms() {

        return AWSKMSClientBuilder.standard()
            .withRegion(Regions.US_EAST_1)
            .withCredentials(awsUtil.getKmsCredentials())
            .build();
    }


    @Bean
    @Profile({"!local"})
    public KmsClient kmsClient() {
        return KmsClient.builder()
            .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
            .region(Region.US_EAST_1)
            .build();
    }


    @Bean
    @Profile({"local"})
    public KmsClient localKmsClient() {
        return KmsClient.builder()
            .credentialsProvider(DefaultCredentialsProvider.create())
            .region(Region.US_EAST_1)
            .build();
    }
}
