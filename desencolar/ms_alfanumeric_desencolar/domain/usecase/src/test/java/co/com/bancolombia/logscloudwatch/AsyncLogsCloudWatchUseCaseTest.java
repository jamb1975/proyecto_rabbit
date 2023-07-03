package co.com.bancolombia.logscloudwatch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AsyncLogsCloudWatchUseCaseTest {

    private AsyncLogsCloudWatchUseCase asyncLogsCloudWatchUseCase;

    @BeforeEach
    public void setup(){
        asyncLogsCloudWatchUseCase = new AsyncLogsCloudWatchUseCase();
    }

    @Test
    public void putLogsCloudWatch(){

        boolean result = asyncLogsCloudWatchUseCase.putLogsCloudWatch("Test", "True", Object.class);
        assertEquals(true, result);

    }

    @Test
    public void putLogsCloudWatchTipoInfo(){

        boolean result = asyncLogsCloudWatchUseCase.putLogsCloudWatch("Test",0, "True", Object.class);
        assertEquals(true, result);

    }

    @Test
    public void putLogsCloudWatchTipoWarn(){

        boolean result = asyncLogsCloudWatchUseCase.putLogsCloudWatch("Test",1, "True", Object.class);
        assertEquals(true, result);

    }

    @Test
    public void putLogsCloudWatchTipoError(){

        boolean result = asyncLogsCloudWatchUseCase.putLogsCloudWatch("Test",2, "True", Object.class);
        assertEquals(true, result);

    }
}
