package co.com.bancolombia;

import co.com.bancolombia.useregister.UseRegisterUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class AlphaNumericDesencolarMain {
   @Autowired
    private  UseRegisterUseCase useRegisterUseCase;
    public static void main(String[] args) {

      //  UseRegisterUseCase useRegisterUseCase = new UseRegisterUseCase();
      //  useRegisterUseCase.consume();
        SpringApplication.run(AlphaNumericDesencolarMain.class, args);
    }

   @Bean
    public CommandLineRunner CommandLineRunnerBean() {
        return (args) -> {
            System.out.println("In CommandLineRunnerImpl ");

           useRegisterUseCase.consume();
            for (String arg : args) {
                System.out.println(arg);
            }
        };
    }
}
