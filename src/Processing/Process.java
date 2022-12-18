package Processing;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * Represents a process to be run on the CPU
 * @author avm33
 */
public class Process {

    /**
     * Ordered queue of the steps that will be executed in the process
     */
    private final Queue<ProcessStep> steps;

    /**
     * Creates an empty process
     */
    public Process(){
        steps = new LinkedList<>();
    }

    /**
     * Adds the given action step to the process
     * @param step The action step to add
     */
    public void addStep(ActionStep step){
        steps.add(step);
    }

    /**
     * Adds the given wait step to the process
     * @param step The wait step to add
     */
    public void addWaitStep(WaitStep step){
        steps.add(step);
    }

    /**
     * Executes the next step in the process
     */
    public void runNextStep(){
        ProcessStep nextStep = Objects.requireNonNull(steps.peek());
        if(nextStep instanceof WaitStep){
            if(!((WaitStep) nextStep).shouldKeepWaiting()){
                steps.poll();
            }
        }else{
            ((ActionStep) steps.poll()).run();
        }
    }

    /**
     * Returns whether all steps in the process have been completed
     * @return Whether all steps in the process have been completed
     */
    public boolean isFinished(){
        return steps.isEmpty();
    }

}
