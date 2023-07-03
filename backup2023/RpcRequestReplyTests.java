package co.com.bancolombia.rabbitmqadapter;

import co.com.bancolombia.rabbitmqadapter.config.RabbitMqConfig;
import com.rabbitmq.client.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.RpcClient;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.rabbitmq.client.ConnectionFactory.DEFAULT_AMQP_OVER_SSL_PORT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//public class RpcRequestReplyTests {

   /* static final List<String> QUEUES = Arrays.asList("queue_calf_create");
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcRequestReplyTests.class);
    Mono<Connection> serverConnection;
    Channel serverChannel;
    RpcServer rpcServer;
    Sender sender;



   private static String HOST_STATIC;


    public static Stream<Arguments> requestReplyParameters() {
        return Stream.of(
                Arguments.of(QUEUES.get(0),
                        (Function<Sender, RpcClient>) sender -> sender.rpcClient("", QUEUES.get(0)))
               );
    }

    @BeforeAll
    public static void initAll() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        LOGGER.info("Port Amqps->{}",DEFAULT_AMQP_OVER_SSL_PORT);
        LOGGER.info("Host->{}", RpcRequestReplyTests.HOST_STATIC);

        connectionFactory.setHost("0.0.0.0");
        connectionFactory.setPort(5673);
        connectionFactory.setUsername("jaamurci");
        connectionFactory.setPassword("JAmbg172");

        connectionFactory.useNio();
        try (Connection c = connectionFactory.newConnection()) {
            Channel ch = c.createChannel();
            for (String queue : QUEUES) {
              // ch.queueDeclare(queue, false, false, false, null);
            }
        }
    }

    @AfterAll
    public static void tearDownAll() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("0.0.0.0");
        connectionFactory.setPort(5673);
        connectionFactory.setUsername("jaamurci");
        connectionFactory.setPassword("JAmbg172");

        connectionFactory.useNio();
        try (Connection c = connectionFactory.newConnection()) {
            Channel ch = c.createChannel();
            for (String queue : QUEUES) {
               // ch.queueDelete(queue);
            }
        }
    }

    @BeforeEach
    public void init() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("0.0.0.0");
        connectionFactory.setPort(5673);
        connectionFactory.setUsername("jaamurci");
        connectionFactory.setPassword("JAmbg172");
        connectionFactory.useNio();
        connectionFactory.setShutdownTimeout(0);

        serverConnection = Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit-client")).cache();
        serverChannel = serverConnection.block().createChannel();
       //sender = RabbitFlux.createSender(new SenderOptions().connectionMono(serverConnection));
    }

    @AfterEach
    public void tearDown() throws Exception {
        LOGGER.info("Cerrar-> conexion");
        if (rpcServer != null) {
            rpcServer.terminateMainloop();
        }
        if (sender != null) {
            sender.close();
        }
    }

    @ParameterizedTest
    @MethodSource("requestReplyParameters")
    public void requestReply(String queue, Function<Sender, RpcClient> rpcClientCreator) throws Exception {
      rpcServer = new TestRpcServer(serverChannel, queue);
        Mono.just("Start thread create")
                .publishOn(Schedulers.newParallel("thread-create"))
                .map(msg -> {
                    LOGGER.info(msg);
                    try {
                       rpcServer.mainloop();
                        return msg;

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .subscribe();*/
   /*     new Thread(() -> {
            try {
                rpcServer.mainloop();
            } catch (Exception e) {
                // safe to ignore when loops ends/server is canceled
            }
        }).start();

        sender = RabbitFlux.createSender(new SenderOptions().connectionMono(serverConnection));

        int nbRequests = 15;
        CountDownLatch latch = new CountDownLatch(nbRequests);
        try (RpcClient rpcClient = rpcClientCreator.apply(sender)) {
            IntStream.range(0, nbRequests).forEach(i -> {
                String content = "hello " + i;
                Mono<Delivery> deliveryMono = rpcClient
                        .rpc(Mono.just(new RpcClient.RpcRequest(content.getBytes())));
                       // .timeout(Duration.ofMillis(7000))
                       // .retry(3);
               // rpcClient.close();
                String received = new String(deliveryMono.block().getBody());
                assertEquals("*** " + content + " ***", received);
                latch.countDown();
            });
            assertTrue(latch.await(5, TimeUnit.SECONDS), "All requests should have dealt with by now");
        }

    }

    private static class TestRpcServer extends RpcServer {

        public TestRpcServer(Channel channel, String queueName) throws IOException {
            super(channel, queueName);
        }

        @Override
        public byte[] handleCall(Delivery request, AMQP.BasicProperties replyProperties) {
            String input = new String(request.getBody());
            LOGGER.info("Recibido->{}", input);

            return ("*** " + input + " ***").getBytes();
        }
    }
}
