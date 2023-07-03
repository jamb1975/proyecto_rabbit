package co.com.bancolombia.useregister;

import co.com.bancolombia.userregister.model.UserRegister;
import co.com.bancolombia.userregister.gateways.UserRegisterRepository;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Log4j2
public class UseRegisterUseCase  {
    private final UserRegisterRepository userRegisterRepository;
   public UseRegisterUseCase(UserRegisterRepository userRegisterRepository){
       this.userRegisterRepository = userRegisterRepository;
   }
   public boolean consume() throws InterruptedException, IOException {

       return userRegisterRepository.consume();
    }
}
