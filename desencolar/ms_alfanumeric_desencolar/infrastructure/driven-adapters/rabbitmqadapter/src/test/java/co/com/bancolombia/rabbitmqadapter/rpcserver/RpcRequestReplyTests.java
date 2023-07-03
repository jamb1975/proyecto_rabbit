package co.com.bancolombia.rabbitmqadapter.rpcserver;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.RpcClient;
import reactor.rabbitmq.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

public class RpcRequestReplyTests {

    static final List<String> QUEUES = Arrays.asList("queue_calf_create");
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcRequestReplyTests.class);
    Mono<Connection> serverConnection;
    Channel serverChannel;
    RpcServer rpcServer;
    Sender sender;

    RpcClient client;

    ChannelPool channelPool;

    Delivery delivery;

    RpcClient.RpcRequest rpcRequest;
   private static String HOST_STATIC;
    public static Stream<Arguments> requestReplyParameters() {
        return Stream.of(
                Arguments.of(QUEUES.get(0),
                        "Msg1"),
                Arguments.of(QUEUES.get(0),
                        "Msg2"),
                Arguments.of(QUEUES.get(0),
                        "Msg3"),
                Arguments.of(QUEUES.get(0),
                        "Msg4"),
                Arguments.of(QUEUES.get(0),
                        "Msg5"),
                Arguments.of(QUEUES.get(0),
                        "Msg6"),
                Arguments.of(QUEUES.get(0),
                        "Msg7")

        );
    }

    @BeforeAll
    public static void initAll() throws Exception {
      /*  ConnectionFactory connectionFactory = new ConnectionFactory();
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
        }*/
    }

    @AfterAll
    public static void tearDownAll() throws Exception {
     /*   ConnectionFactory connectionFactory = new ConnectionFactory();
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
        }*/
    }

    @BeforeEach
    public void init() throws Exception {
        LOGGER.info("Init-> conexion");
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("0.0.0.0");
        connectionFactory.setPort(5673);
        connectionFactory.setUsername("jaamurci");
        connectionFactory.setPassword("JAmbg172");
        connectionFactory.useNio();
        connectionFactory.setShutdownTimeout(0);
        connectionFactory.setChannelShouldCheckRpcResponseType(true);
       // connectionFactory.setChannelRpcTimeout(1000);
        connectionFactory.setRequestedChannelMax(2);
        serverConnection = Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit-client")).cache();

         channelPool = ChannelPoolFactory.createChannelPool(
                            serverConnection,
                            new ChannelPoolOptions().maxCacheSize(5)
        );
        serverChannel = serverConnection.block().createChannel();
         sender = RabbitFlux.createSender(new SenderOptions().connectionMono(serverConnection).channelPool(channelPool));
         client =  sender.rpcClient("", "queue_calf_create");


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
    public void requestReply(String queue, String queue2) throws Exception {
      rpcServer = new TestRpcServer(serverChannel, "queue_calf_create");
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
                .subscribe();
       new Thread(() -> {
            try {
                rpcServer.mainloop();
            } catch (Exception e) {
                // safe to ignore when loops ends/server is canceled
            }
        }).start();

        //sender = RabbitFlux.createSender(new SenderOptions().connectionMono(serverConnection));
     /*
        int nbRequests = 15;
        CountDownLatch latch = new CountDownLatch(nbRequests);
        try (RpcClient rpcClient = rpcClientCreator.apply(sender)) {
            IntStream.range(0, nbRequests).forEach(i -> {
                String content = "hello " + i;
               Mono<Delivery> deliveryMono = rpcClient
                        .rpc(Mono.just(new RpcClient.RpcRequest(content.getBytes())));
                       // .timeout(Duration.ofMillis(7000))
                       // .retry(3);

                String received = new String(deliveryMono.block().getBody());
                //rpcClient.close();
                assertEquals("*** " + content + " ***", received);
                latch.countDown();
                //rpcClient.close();
            });

            assertTrue(latch.await(5, TimeUnit.SECONDS), "All requests should have dealt with by now");
            //rpcClient.close();
        }

        Thread.sleep(30000);*/
        Delivery delivery2 = client.rpc(Mono.just(new RpcClient.RpcRequest(queue2.getBytes(StandardCharsets.UTF_8)))).log().block();
        client.close();
        channelPool.close();
        sender.close();
        serverChannel.close();
        LOGGER.info(new String(delivery2.getBody(), StandardCharsets.UTF_8).concat("-Delivery"));
       /* delivery.flatMap(reply ->
             Mono.just(new String(reply.getBody(), StandardCharsets.UTF_8).concat("-Delivery"))
        ).log().subscribe();*/

        //client.close();

        /*Flux.fromIterable(Arrays.asList("foo", "bar", "foobar")).log()
         .flatMap(t2queuename ->  Mono.just(sender.rpcClient("", "queue_calf_create", () -> UUID.randomUUID().toString()))

                .flatMap(rpc ->
                        rpc.rpc(Mono.just(new RpcClient.RpcRequest(t2queuename.getBytes(StandardCharsets.UTF_8))))

                                .flatMap(reply -> {

                                     return Mono.just(new String(reply.getBody(), StandardCharsets.UTF_8).concat("-Delivery")).log();
                                })
                                .subscribeOn(Schedulers.boundedElastic())
                )
        ).subscribe();*/
        Thread.sleep(7000);

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
