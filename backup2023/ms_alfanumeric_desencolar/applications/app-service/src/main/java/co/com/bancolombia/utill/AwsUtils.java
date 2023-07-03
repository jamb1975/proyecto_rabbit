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

        //Credenciales de desarrollo
       BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
               "ASIA5MK4RYC3RJSX2RX5",
               "+IdWANVBJLYxTADAoBF98+NiS9HEinyyOU++ph3o",
               "IQoJb3JpZ2luX2VjEDQaCXVzLWVhc3QtMSJIMEYCIQCjWlGv9bFum0cnxFrHQLBnNxYhmdYgNJQv8yjGd/aLyQIhAOwHncCX4t8ql0hVyd9scybTa3hve2w/Whk7vhvSbsqfKp0DCN3//////////wEQAxoMOTE5ODUzOTczNjg3IgyYwj0tkSGtGl8wTEoq8QIHpNOnBrMlsAF8ISPd/I2Epd/t9shfcxF77Huop7oTKWGwI3LfCvDKQs3ltcWGh02OmVFaWhOGTW0un5qU7BJYOaEZ3N9TILCnyamWRWwSU/TSfDM35KQiLyJpAwxuWdeQz4kVqcpfJEVTsvay0K3i4hH/N8J6s4rqOTXDezXcisG/5vC3sEEEwQ+FIhAK50FZ2qm7lmWeteWoq1OiQtHkr3ln+J29C62+Z765OTx6FwyT5PuOWL7vFdUWfbo9hosh+fKFn7Uj7LtAxnTEGnByDKZjLrHqkbm4756LSVUs6id7E/Tr3QPJSInt5g+joNZCyjrWze8Sw3e14B3BCZyo+kCMkkerASvPWvICRJmjOCR0xGBiBXZyexLMD9+FuG2b9q9XMDZrIbxK9pPCZcTTPgPl8l+NDEKgjimRvnnt/tQxWYtx85EZ4a6+KE0GVUDI6offlYBPN3gHASNO4LPUCxf7i5BnHvsttPa6Y6bLrQAw7reOoAY6pQFg2JxVARFPlVLJPOxsidn4TD36OU0LOtgeGKgn8jkSLqwm1xuacU9MUrmMrEUX9ha8QLGiJEG3MlCgCi9Xk41d5blOvlMu7LIHtFBe1REFmSZMoNaT5ng/CoIj2YeZ0Y6rfLTcdoVE5mB49GM57MTAOMDXHUxTTi0NxoKTbPUJ/u/V+q13HI1NORxJLUYFlA14TPWA+2LoItkQcIVxJOpCJpx7KFg=");
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
