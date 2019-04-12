package my.sheshenya.blockhound;

import org.junit.Before;
import org.junit.Test;
import reactor.BlockHound;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class ReactorBlockhoundApplicationTests {


    @Before
    public void setup(){
        BlockHound.install();
    }

    @Test
    public void method1_with_blocking_code() {
        Mono.delay(Duration.ofSeconds(1))
                .doOnNext(it -> {
                    try {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .block();
    }


    @Test
    public void method2_with_blocking_code() {
        Flux.range(0, Runtime.getRuntime().availableProcessors() * 2)
                .subscribeOn(Schedulers.parallel())
                .map(i -> {
                    CountDownLatch latch = new CountDownLatch(1);

                    Mono.delay(Duration.ofMillis(i * 10))
                            .subscribe(it -> latch.countDown());

                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    return i;
                })
                .blockLast();
    }

}
