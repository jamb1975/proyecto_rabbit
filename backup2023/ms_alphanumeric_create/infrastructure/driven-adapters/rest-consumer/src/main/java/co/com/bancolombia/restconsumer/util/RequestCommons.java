package co.com.bancolombia.restconsumer.util;

import co.com.bancolombia.cognito.signin.model.Constants;
import co.com.bancolombia.exeption.AppException;
import co.com.bancolombia.exeption.BusinessException;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Response;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestCommons {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestCommons.class);
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;
    private static RestTemplate restTemplate;

    static {
        try {
            restTemplate = restTemplate();
        } catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException e) {
            throw new AppException("Error creando instancia de restTemplate", e);
        }
    }

    private RequestCommons() {
    }

    public static void setRestTemplate(RestTemplate mockRestTemplate) {
        restTemplate = mockRestTemplate;
    }

    public static <T> T processCall(Call<T> call, Class<T> returnType) {
        return processCall(call, returnType, null);
    }


    public static <T> T processCall(Call<T> call, Class<T> returnType, Map<String, String> params) {
        try {
            Response<T> response = executeCall(call.request().url().toString(),
                    Optional.ofNullable(call.request().body()).map(RequestCommons::getStringBody)
                            .orElse(null),
                    getHeaders(call.request().headers()), call.request().method(), returnType, params);
            if (!response.isSuccessful()) {
                return (response.errorBody() != null) ?
                        Constants.GSON.fromJson(response.errorBody().string(), returnType) :
                        null;
            }
            return response.body();
        } catch (Exception e) {
            LOGGER.error(
                    String.format("Error llamando a un servicio externo %s", call.request().url()), e);
            return null;
        }

    }

    private static String getStringBody(RequestBody body) {
        try {
            final Buffer buffer = new Buffer();
            body.writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException e) {
            return null;
        }
    }

    private static HttpHeaders getHeaders(Headers headers) {
        HttpHeaders newHeaders = new HttpHeaders();
        newHeaders.add(Constants.CONTENT_TYPE_HEADER, "application/json; charset=utf-8");
        headers.names().forEach(x -> newHeaders.add(x, headers.get(x)));
        return newHeaders;
    }

    public static <T> Response<T> executeCall(String url, String body, HttpHeaders headers,
                                              String method, Class<T> returnType, Map<String, String> params) {
        ResponseEntity<String> response;
        if (params == null) {
            params = new HashMap<String, String>();
        }
        try {
            HttpEntity<String> request = new HttpEntity<>(body, headers);
            response = restTemplate.exchange(url,
                    Optional.ofNullable(HttpMethod.resolve(method)).orElse(HttpMethod.POST), request,
                    String.class, params);
            return Response.success(response.getStatusCode().value(),
                    Constants.GSON.fromJson(response.getBody(), returnType));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            String bodyResponse = e.getResponseBodyAsString();
            LOGGER.warn("Respuesta negativa controlada de servicio externo: code {} {}, error: {}",
                    e.getStatusText(), e, e.getLocalizedMessage() + " Url  : " + url);
            return Response.error(e.getRawStatusCode(),
                    ResponseBody.create(MediaType.get("application/json; charset=utf-8"),
                            bodyResponse));
        }
    }

    public static RestTemplate restTemplate()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        requestFactory.setConnectTimeout(CONNECTION_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);
        return new RestTemplate(requestFactory);
    }

    public static <T> T processGetAliasCall(Call<T> call, Class<T> returnType) {
        try {
            var response = executeCall(call.request().url().toString(),
                    Optional.ofNullable(call.request().body()).map(RequestCommons::getStringBody)
                            .orElse(null),
                    getHeaders(call.request().headers()), call.request().method(), returnType, null);
            if (!response.isSuccessful()) {
                return (response.errorBody() != null) ?
                        Constants.GSON.fromJson(response.errorBody().string(), returnType) :
                        null;
            }
            if (response.code() == Constants.NOT_CONTENT) {
                LOGGER.info("not content: " + Optional.ofNullable(call.request().body())
                                .map(RequestCommons::getStringBody)
                                .orElse(null) + "Headers: " + getHeaders(call.request().headers()),
                        call.request().method());
                throw new BusinessException(Constants.USER_NOT_AVAILABLE);
            }
            return response.body();
        } catch (BusinessException e) {
            throw new BusinessException(Constants.USER_NOT_AVAILABLE);
        } catch (Exception e) {
            LOGGER.error(
                    String.format("Error llamando a un servicio externo %s", call.request().url()), e);
            return null;
        }
    }

}

