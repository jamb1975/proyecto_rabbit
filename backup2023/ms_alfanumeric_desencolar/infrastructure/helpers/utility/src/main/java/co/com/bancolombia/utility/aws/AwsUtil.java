package co.com.bancolombia.utility.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AwsUtil {

    private static final String LOCAL = "local";
    private final AwsProperties awsProperties;
    private final Environment environment;

    public AWSCredentialsProvider getKmsCredentials() {
        if (Arrays.stream(environment.getActiveProfiles())
            .anyMatch(env -> (env.equalsIgnoreCase(LOCAL)))) {
            return new AWSStaticCredentialsProvider(new BasicSessionCredentials(
                awsProperties.getAccessKey(),
                awsProperties.getSecretKey(),
                awsProperties.getSessionToken()));
        } else {
            return WebIdentityTokenCredentialsProvider.create();
        }
    }

}