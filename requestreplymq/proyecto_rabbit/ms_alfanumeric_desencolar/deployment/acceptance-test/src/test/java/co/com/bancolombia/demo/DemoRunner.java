package co.com.bancolombia.demo;

import com.intuit.karate.junit5.Karate;

public class DemoRunner {

    @Karate.Test
    Karate demo() {
        return Karate.run("demo").relativeTo(getClass());
    }
}
