package Demos;

import Processing.CPU;
import Processing.Process;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This program demonstrates a scenario in which the fix shown in the previous program fails. In this scenario,
 * processes A and B have second critical sections that start right as C's critical section finishes. As seen in the
 * output, another deadlock occurs where neither A nor B's second critical sections can run.
 */
public class Petersons3ProcessesIncorrectFixFailing {

    public static void main(String[] args){
        //Initialize process locks and turn tracker:
        AtomicBoolean lockA = new AtomicBoolean(false);
        AtomicBoolean lockB = new AtomicBoolean(false);
        AtomicBoolean lockC = new AtomicBoolean(false);
        AtomicReference<Character> turn = new AtomicReference<>('A');

        //Create process A:
        Process a = new Process();
        //Add first critical section:
        a.addStep(() -> lockA.set(true));
        a.addStep(() -> turn.set('B'));
        a.addWaitStep(() -> (lockB.get() || lockC.get()) && turn.get() != 'A');
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 1/3"));
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 2/3"));
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 3/3"));
        a.addStep(() -> lockA.set(false));
        a.addStep(() -> turn.set('B'));
        //Add non-critical section:
        for(int i = 1; i <= 8; i++){
            /*
             * Eight steps causes this non-critical section to finish just as C's critical section finishes, which is
             * when A and B's second critical sections should start
             */
            int finalI = i;
            a.addStep(() -> System.out.println("Process A non-critical section " + finalI + "/8"));
        }
        //Add second critical section:
        a.addStep(() -> lockA.set(true));
        a.addStep(() -> turn.set('B'));
        a.addWaitStep(() -> (lockB.get() || lockC.get()) && turn.get() != 'A');
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION #2 1/3"));
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION #2 2/3"));
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION #2 3/3"));
        a.addStep(() -> lockA.set(false));
        a.addStep(() -> turn.set('B'));

        //Create process B:
        Process b = new Process();
        //Add first critical section:
        b.addStep(() -> lockB.set(true));
        b.addStep(() -> turn.set('C'));
        b.addWaitStep(() -> (lockA.get() || lockC.get()) && turn.get() != 'B');
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 1/3"));
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 2/3"));
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 3/3"));
        b.addStep(() -> lockB.set(false));
        b.addStep(() -> turn.set('C'));
        //Add non-critical section:
        b.addStep(() -> System.out.println("Process B non-critical section 1/3"));
        b.addStep(() -> System.out.println("Process B non-critical section 2/3"));
        b.addStep(() -> System.out.println("Process B non-critical section 3/3"));
        //Add second critical section:
        b.addStep(() -> lockB.set(true));
        b.addStep(() -> turn.set('C'));
        b.addWaitStep(() -> (lockA.get() || lockC.get()) && turn.get() != 'B');
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION #2 1/3"));
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION #2 2/3"));
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION #2 3/3"));
        b.addStep(() -> lockB.set(false));
        b.addStep(() -> turn.set('C'));

        //Create process C:
        Process c = new Process();
        c.addStep(() -> lockC.set(true));
        c.addStep(() -> turn.set('A'));
        c.addWaitStep(() -> (lockA.get() || lockB.get()) && turn.get() != 'C');
        c.addStep(() -> System.out.println("PROCESS C CRITICAL SECTION 1/3"));
        c.addStep(() -> System.out.println("PROCESS C CRITICAL SECTION 2/3"));
        c.addStep(() -> System.out.println("PROCESS C CRITICAL SECTION 3/3"));
        c.addStep(() -> lockC.set(false));
        c.addStep(() -> turn.set('A'));

        //Run processes:
        CPU.runConcurrentProcesses(a, b, c);

        /*
         * The deadlock occurs because process B sets the turn to C, but since C doesn't have a second critical section,
         * The turn doesn't get set back to A, so processes A and B are stuck busy-waiting. At this point, it is
         * reasonable to conclude that Petersons two-process algorithm cannot be stretched to fit three processes, and
         * a more complex algorithm is necessary.
         */
    }

}
