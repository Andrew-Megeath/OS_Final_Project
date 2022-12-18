package Demos;

import Processing.CPU;
import Processing.Process;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This program demonstrates Peterson's algorithm functioning correctly with two processes. As seen in the output,
 * Peterson's algorithm ensures that the critical sections of both processes are mutually exclusive.
 */
public class Petersons2Processes {
    
    public static void main(String[] args){
        //Initialize process locks and turn tracker:
        AtomicBoolean lockA = new AtomicBoolean(false);
        AtomicBoolean lockB = new AtomicBoolean(false);
        AtomicReference<Character> turn = new AtomicReference<>('A');
        //Atomic objects are used above because references to local variables aren't allowed in lambda expressions

        //Create process A:
        Process a = new Process();
        a.addStep(() -> lockA.set(true));
        a.addStep(() -> turn.set('B'));
        a.addWaitStep(() -> lockB.get() && turn.get() != 'A'); //this is the equivalent of "while(lockB && turn != A);"
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 1/3"));
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 2/3"));
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 3/3"));
        a.addStep(() -> lockA.set(false));

        //Create process B:
        Process b = new Process();
        b.addStep(() -> lockB.set(true));
        b.addStep(() -> turn.set('A'));
        b.addWaitStep(() -> lockA.get() && turn.get() != 'B');
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 1/3"));
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 2/3"));
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 3/3"));
        b.addStep(() -> lockB.set(false));

        //Run processes:
        CPU.runConcurrentProcesses(a, b);
    }
    
}
