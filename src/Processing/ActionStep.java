package Processing;

/**
 * Represents a step in a process that performs an action
 * @author avm33
 */
public interface ActionStep extends ProcessStep {

    /**
     * Executes the step
     */
    void run();

}
