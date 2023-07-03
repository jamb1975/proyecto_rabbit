package co.com.bancolombia.config.beans;

import co.com.bancolombia.kms.gateways.AsymmetricKmsAdapter;
import co.com.bancolombia.userregister.model.UserRegister;
import co.com.bancolombia.userregister.gateways.UserRegisterRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@Log4j2
public class UserRegisterBeansConfig {

    @Bean
    @ConditionalOnMissingBean
     public UserRegisterRepository userRegisterRepository(){
        log.warn("configuracion true");
        return userRegisterRepository();
    }



    private final  UserRegisterRepository userRegisterRepository = new UserRegisterRepository() {
        @Override
        public Mono<List<UserRegister>> getReadMq() {
            return null;
        }

        @Override
        public void consume() {

        }
    };

    /*@Bean
    //@ConditionalOnMissingBean
    public AsymmetricKmsAdapter asymmetricKmsAdapter(){
        log.warn("configuracion true");
        return asymmetricKmsAdapter();
    }
    private final  AsymmetricKmsAdapter asymmetricKmsAdapter = new AsymmetricKmsAdapter() {
        @Override
        public String decryptWithKms(String encryptedPart) {
            return null;
        }

        @Override
        public String encryptWithKms(String encryptedPart) {
            return null;
        }


    };*/
}
