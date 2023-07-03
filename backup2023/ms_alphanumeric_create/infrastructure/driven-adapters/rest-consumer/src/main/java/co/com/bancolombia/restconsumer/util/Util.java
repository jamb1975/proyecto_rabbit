package co.com.bancolombia.restconsumer.util;


import co.com.bancolombia.cognito.signin.model.Constants;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

public class Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

    private Util() {
    }

    public static <T> T processCall(final Call<T> call, final Class<T> returnType) {
        try {
            Response<T> response = call.execute();
            if (!response.isSuccessful()) {
                return (response.body() != null) ?
                    Constants.GSON.fromJson(response.body().toString(), returnType) :
                    null;
            }
            return response.body();
        } catch (Exception e) {
            LOGGER.error(
                String.format("Error llamando a un servicio externo %s", call.request().url()));
            return null;
        }
    }
}
