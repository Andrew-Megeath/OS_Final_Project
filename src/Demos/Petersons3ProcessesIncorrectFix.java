package Demos;

import Processing.CPU;
import Processing.Process;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This program demonstrates the seemingly-obvious yet not completely correct fix that allows Peterson's two-process
 * algorithm to successfully run three processes. While it works in the simulation below, there are situations that will
 * be shown in the next program where this fix fails.
 *
 * The fix is to change the turn to the next process after unlocking a process. This is a quick way out of the deadlock
 * seen in the previous program.
 */
public class Petersons3ProcessesIncorrectFix {

    public static void main(String[] args){
        //Initialize process locks and turn tracker:
        AtomicBoolean lockA = new AtomicBoolean(false);
        AtomicBoolean lockB = new AtomicBoolean(false);
        AtomicBoolean lockC = new AtomicBoolean(false);
        AtomicReference<Character> turn = new AtomicReference<>('A');

        //Create process A:
        Process a = new Process();
        a.addStep(() -> lockA.set(true));
        a.addStep(() -> turn.set('B'));
        a.addWaitStep(() -> (lockB.get() || lockC.get()) && turn.get() != 'A');
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 1/3"));
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 2/3"));
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 3/3"));
        a.addStep(() -> lockA.set(false));
        a.addStep(() -> turn.set('B')); //the fix

        //Create process B:
        Process b = new Process();
        b.addStep(() -> lockB.set(true));
        b.addStep(() -> turn.set('C'));
        b.addWaitStep(() -> (lockA.get() || lockC.get()) && turn.get() != 'B');
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 1/3"));
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 2/3"));
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 3/3"));
        b.addStep(() -> lockB.set(false));
        b.addStep(() -> turn.set('C')); //the fix

        //Create process C:
        Process c = new Process();
        c.addStep(() -> lockC.set(true));
        c.addStep(() -> turn.set('A'));
        c.addWaitStep(() -> (lockA.get() || lockB.get()) && turn.get() != 'C');
        c.addStep(() -> System.out.println("PROCESS C CRITICAL SECTION 1/3"));
        c.addStep(() -> System.out.println("PROCESS C CRITICAL SECTION 2/3"));
        c.addStep(() -> System.out.println("PROCESS C CRITICAL SECTION 3/3"));
        c.addStep(() -> lockC.set(false));
        c.addStep(() -> turn.set('A')); //the fix

        //Run processes:
        CPU.runConcurrentProcesses(a, b, c);
    }

}
