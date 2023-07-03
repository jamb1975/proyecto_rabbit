package co.com.bancolombia.useregister;

import co.com.bancolombia.userregister.gateways.UserRegisterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class UseRegisterUseCaseTest {

    private UseRegisterUseCase useRegisterUseCase;
    private UserRegisterRepository userRegisterRepository;

    @BeforeEach
    public void setup(){
        userRegisterRepository = mock(UserRegisterRepository.class);
        useRegisterUseCase = new UseRegisterUseCase(userRegisterRepository);
    }

    @Test
    public void consumer() throws IOException, InterruptedException {

            boolean result = useRegisterUseCase.consume();
            assertEquals(false, result);

    }
}
