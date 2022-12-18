package Processing;

/**
 * Represents the CPU that the processes are being run on
 * @author avm33
 */
public class CPU {

    /**
     * Runs the given processes concurrently, with one step from each process being run at a time
     * @param processes The processes to run
     */
    public static void runConcurrentProcesses(Process... processes){
        boolean atLeastOneNotFinished;
        do{
            atLeastOneNotFinished = false;
            for(Process process : processes){
                if(!process.isFinished()){
                    atLeastOneNotFinished = true;
                    process.runNextStep();
                }
            }
        }while(atLeastOneNotFinished);
    }

}
