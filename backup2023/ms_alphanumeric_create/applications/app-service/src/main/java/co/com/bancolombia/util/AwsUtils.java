package co.com.bancolombia.util;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.exeption.AppException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AwsUtils {

    public static final String AWS_REGION = "us-east-1";
    public static final String SECRET_MANAGER_SERVICE_ENDPOINT = "secretsmanager.us-east-1.amazonaws.com";
    public static final String KMS_SERVICE_ENDPOINT = "kms.us-east-1.amazonaws.com";
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsUtils.class);
    private static final AWSSecretsManager secretManagerClient;

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
        //Credenciales
        BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
                "ASIA5MK4RYC3UDTRGVGC",
                "37HNiIpUkpssmGa3xbX8urIrZLUQCftkjZAmYuNS",
                "IQoJb3JpZ2luX2VjEDcaCXVzLWVhc3QtMSJIMEYCIQCvnXrv/Y6hgQO2sXNzFFEZiprmWc9XjwyGmeqz3YqH3gIhAOQRdTB0u3ieunMLMqeCRodGJdplYmAUn0xJ/W9NBRR3Kp0DCN///////////wEQAxoMOTE5ODUzOTczNjg3IgwfLGjgcgqUqcfiKNoq8QKWQfWtfEsZ58L6iBm5rHn/KaDzCNtqzF07rqFz2tZNvduOvNzeOzzmnFJIUB3+qm1ZG7NgkY98EDA+TSYRCzftE9v13h+VkuWxh/SPsQR2jULCTlHDgigLl/XIkVlYPy6BR+e/C5V57B9DIeEaqfvNoe2ogeo540Drl82Fn3+z+X9zdBnxXd+bPxk2RXrHpN0rDcu2TKxpSL1U5gkgsCEP6kZFPTnjwvio70V4Mdhg+88je8DQu68Z2EKkReKUCeV/OCWMLgZZ1JCkGVIwcHqDyFvYdHTHNziAG7FLceJtrDBFqr0KzzPHzGbJNtQIYKNd/s+6MR3KXtyTEgzOWswo5Op3b6vcZ2/K+r0/jgUvYMWOdthyqgbCFdQNnJi9mGax9aP+o/y+22gZhD4YeT+WFZNogoJaeb1aZwVDfe08+bbCFQ5+I+O4PkHSa+gHm9sdfsjSKomXjhiahcjwmpQBrNFeZyjsbDr/ZBFR0MQUvVkwvoOPoAY6pQHZeG2RRIhV+3VXAM2He7hyxuSAjrWDqNkI4XZXNI8DYsiem7Ie/lZnAk8tc0sffMHvt9+bQ1xcf0mM+Tq9R+2I7FwNPWEzm4GKI7BkwE2iNLORrSgM0NbcpEKj+MOqhsWEqr0us1KI94XpmKX6svDEudgF7WNyVgfy5UkstTtGBrwjyOi4hEbgUg0nyVgFKhktWmh2OrxvZ4QZ76GSRolNba/hu+U=");
        return new AWSStaticCredentialsProvider(basicSessionCredentials);
        //return WebIdentityTokenCredentialsProvider.create();
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
