package co.com.bancolombia.useregister;

import co.com.bancolombia.userregister.model.UserRegister;
import co.com.bancolombia.userregister.gateways.UserRegisterRepository;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

//@AllArgsConstructor
@Data
public class UseRegisterUseCase  {
    private final UserRegisterRepository userRegisterRepository;


    public Mono<List<UserRegister>> getReadMq(){

        return userRegisterRepository.getReadMq();
    }

    public void consume() throws InterruptedException, IOException {
        userRegisterRepository.consume();
    }
}
