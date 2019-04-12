package my.sheshenya.blockhound;

import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;

public class ReactorBlockhoundHooksApplicationTests {


    /*
    * ReactorDebugAgent from the reactor-tools project is a Java agent that helps you
    * debug exceptions in your application
    * without paying a runtime cost (unlike Hooks.onOperatorDebug()).
    *
     */
    @Before
    public void setup(){
//        ReactorDebugAgent.init();
//        ReactorDebugAgent.processExistingClasses();

        Hooks.onOperatorDebug();
    }

    @Test
    public void method1_with_IndexOutOfBounds() {

        Flux.range(0, 5)
                    .single() // <-- Aha!
                    .subscribeOn(Schedulers.parallel())
                    .block();
    }


}
