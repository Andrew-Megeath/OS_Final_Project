package Demos;

import Processing.CPU;
import Processing.Process;

/**
 * This program demonstrates what happens when two processes with critical sections are run concurrently without a
 * locking system. As seen in the output, the critical sections of the two processes overlap, which can lead to serious
 * issues.
 */
public class NoLocks2Processes {

    public static void main(String[] args){
        //Create process A:
        Process a = new Process();
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 1/3")); //this adds a step to the process
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 2/3")); //steps are executed in the order added
        a.addStep(() -> System.out.println("PROCESS A CRITICAL SECTION 3/3"));

        //Create process B:
        Process b = new Process();
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 1/3"));
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 2/3"));
        b.addStep(() -> System.out.println("PROCESS B CRITICAL SECTION 3/3"));

        //Run processes:
        CPU.runConcurrentProcesses(a, b);
    }

}
