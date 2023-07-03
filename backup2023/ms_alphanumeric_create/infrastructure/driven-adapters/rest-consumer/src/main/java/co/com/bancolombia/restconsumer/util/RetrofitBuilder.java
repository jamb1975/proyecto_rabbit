package co.com.bancolombia.restconsumer.util;

import co.com.bancolombia.secret.ServicesCredentials;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrofitBuilder.class);
    private static final int READ_TIMEOUT = 10;
    private static OkHttpClient.Builder httpClientBuilder;

    private RetrofitBuilder() {
    }

    public static Retrofit getInstance(String baseUrl, ServicesCredentials credentials) {
        if (httpClientBuilder == null) {
            httpClientBuilder = new OkHttpClient.Builder().readTimeout(READ_TIMEOUT,
                TimeUnit.SECONDS);
            initSSL(credentials.getKeyStore().getKeystorePassword(),
                credentials.getKeyStore().getKeystoreName());
        }
        return new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClientBuilder.build())
            .build();
    }

    private static void initSSL(String pass, String keyStoreName) {
        SSLContext sslContext = null;
        try {
            sslContext = getKeyStore(pass, keyStoreName);
        } catch (CertificateException | IOException | KeyStoreException |
            KeyManagementException | NoSuchAlgorithmException e) {
            LOGGER.error("Error loading keystore", e);
        }
        if (sslContext != null) {
            httpClientBuilder.sslSocketFactory(sslContext.getSocketFactory(),
                systemDefaultTrustManager());
        }
    }

    private static SSLContext getKeyStore(String pass, String keyStoreName)
        throws CertificateException,
        IOException, KeyStoreException, KeyManagementException, NoSuchAlgorithmException {
        InputStream keyStoreFile = new ClassPathResource(
            keyStoreName).getInputStream();
        KeyStore keyStore = null;
        try (InputStream is = keyStoreFile) {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(is, pass.toCharArray());
        } catch (Exception e) {
            LOGGER.error("Cannot load keystore file", e);
        }
        // creating a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext;
    }

    private static X509TrustManager systemDefaultTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException(
                    "Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (GeneralSecurityException e) {
            LOGGER.error("The system has no TLS", e);
            throw new AssertionError();
        }
    }
}
