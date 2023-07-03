package co.com.bancolombia.userregister.gateways;

import co.com.bancolombia.userregister.model.UserRegister;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface UserRegisterRepository {

    public  boolean consume() throws InterruptedException, IOException;
}
