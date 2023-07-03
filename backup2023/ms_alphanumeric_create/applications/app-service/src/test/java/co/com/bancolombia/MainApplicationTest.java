package co.com.bancolombia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MainApplicationTest {


    @Autowired
    ConfigurableApplicationContext applicationContext;

    /*@Test
    void workerStarter() {
        applicationContext.start();
        boolean isCtxRunning = applicationContext.isRunning();
        assertThat(isCtxRunning).isTrue();
        applicationContext.registerShutdownHook();
    }*/
}
