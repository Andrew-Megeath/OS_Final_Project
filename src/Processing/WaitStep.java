package Processing;

/**
 * Represents a busy-waiting loop in a process
 * @author avm33
 */
public interface WaitStep extends ProcessStep {

    /**
     * Returns whether the loop should keep waiting
     * @return Whether the loop should keep waiting
     */
    boolean shouldKeepWaiting();

}
