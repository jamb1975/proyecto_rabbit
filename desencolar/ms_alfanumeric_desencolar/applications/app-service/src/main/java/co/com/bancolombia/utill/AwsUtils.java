package co.com.bancolombia.utill;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.exception.AppException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.nio.ByteBuffer;

public final class AwsUtils {

    public static final String AWS_REGION = "us-east-1";
    public static final String SECRET_MANAGER_SERVICE_ENDPOINT = "secretsmanager.us-east-1.amazonaws.com";
    public static final String KMS_SERVICE_ENDPOINT = "kms.us-east-1.amazonaws.com";
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsUtils.class);
    private static final AWSSecretsManager secretManagerClient;
    @Value("aws.session.credentials.accessKey")
    private static String accessKey;

    @Value("aws.session.credentials.secretKey")
    private static String secretKey;

    @Value("aws.session.credentials.sessionToken")
    private static String sessionToken;

    static {
        AwsClientBuilder.EndpointConfiguration config =
            new AwsClientBuilder.EndpointConfiguration(SECRET_MANAGER_SERVICE_ENDPOINT, AWS_REGION);
        AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
        clientBuilder.withCredentials(getCredentials()).setEndpointConfiguration(config);
        secretManagerClient = clientBuilder.build();
    }

    private AwsUtils() {
    }

    public static AWSCredentialsProvider getCredentials() {
        return WebIdentityTokenCredentialsProvider.create();
    }

    public static <T> T getSecret(String awsDbSecret, Class<T> result) {

        var getSecretValueRequest = new GetSecretValueRequest()
            .withSecretId(awsDbSecret).withVersionStage("AWSCURRENT");
        GetSecretValueResult getSecretValueResult = null;
        try {
            getSecretValueResult = secretManagerClient.getSecretValue(getSecretValueRequest);

        } catch (ResourceNotFoundException e) {
            LOGGER.error(String.format("The requested secret %s was not found", awsDbSecret), e);
        } catch (InvalidRequestException e) {
            LOGGER.error(String.format("The request was invalid due to: %s", e.getMessage()), e);
        } catch (InvalidParameterException e) {
            LOGGER.error(String.format("The request had invalid params: %s", e.getMessage()), e);
        }

        if (getSecretValueResult == null) {
            throw new AppException("Error leyendo el secreto " + awsDbSecret);
        }

        String secret;
        ByteBuffer binarySecretData;
        if (getSecretValueResult.getSecretString() != null) {
            secret = getSecretValueResult.getSecretString();
        } else {
            binarySecretData = getSecretValueResult.getSecretBinary();
            secret = binarySecretData.toString();
        }

        return Constants.GSON.fromJson(secret, result);
    }
}
