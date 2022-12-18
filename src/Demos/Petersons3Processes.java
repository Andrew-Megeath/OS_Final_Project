package Demos;

import Processing.CPU;
import Processing.Process;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This program demonstrates what happens when one attempts to apply Peterson's two-process mechanism to three
 * processes. Some minor necessary changes were made to accommodate the third process, but the basic functionality of
 * the algorithm is still very much the same. These changes will be pointed out below. As seen in the output, this
 * method results in a deadlock where once process A finishes, neither B nor C can run.
 */
public class Petersons3Processes {

    public static void main(String[] args){
        //Initialize process locks and turn tracker:
        AtomicBoolean lockA = new AtomicBoolean(false);
        AtomicBoolean lockB = new AtomicBoolean(false);
        AtomicBoolean lockC = new AtomicBoolean(false); //lock for third process
        AtomicReference<Character> turn = new AtomicReference<>('A');

        //Create process A:
        Process a = new Process();
        a.addStep(() -> lockA.set(true));
        a.addStep(() -> turn.set('B'));
        a.addWaitStep(() -> (lockB.get() || lockC.get()) && turn.get() != 'A'); //now checks for process C's lock too
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 1/3"));
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 2/3"));
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 3/3"));
        a.addStep(() -> lockA.set(false));

        //Create process B:
        Process b = new Process();
        b.addStep(() -> lockB.set(true));
        b.addStep(() -> turn.set('C')); //sets turn to C instead of A
        b.addWaitStep(() -> (lockA.get() || lockC.get()) && turn.get() != 'B'); //also now checks for process C's lock
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 1/3"));
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 2/3"));
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 3/3"));
        b.addStep(() -> lockB.set(false));

        //Create process C:
        Process c = new Process();
        c.addStep(() -> lockC.set(true));
        c.addStep(() -> turn.set('A')); //brings turn back to A
        c.addWaitStep(() -> (lockA.get() || lockB.get()) && turn.get() != 'C');
        c.addStep(() -> System.out.println("PROCESS C CRITICAL SECTION 1/3"));
        c.addStep(() -> System.out.println("PROCESS C CRITICAL SECTION 2/3"));
        c.addStep(() -> System.out.println("PROCESS C CRITICAL SECTION 3/3"));
        c.addStep(() -> lockC.set(false));

        //Run processes:
        CPU.runConcurrentProcesses(a, b, c);

        /*
         * Here's why the deadlock occurs: once process A has finished, the turn is still set to A, and processes B and
         * C are still locked. Since B and C will not be given their turns, they must wait for each other's locks to be
         * turned off, so they are stuck waiting indefinitely. Changing the ors to ands in B and C's busy-wait loops
         * only results in their critical sections being run simultaneously.
         */
    }

}
