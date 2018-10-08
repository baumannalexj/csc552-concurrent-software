package ajeffrey.teaching.dining;

public class DinerTokenSemaphore {

    private int numberOfAvailableTokens;

    public DinerTokenSemaphore(int numberOfDiners) {
        this.numberOfAvailableTokens = numberOfDiners - 1;
    }


    public synchronized boolean acquire() {
        if (numberOfAvailableTokens > 0) {
            numberOfAvailableTokens--;
            System.out.println("token acquired. " + numberOfAvailableTokens + " tokens left.");
            return true;
        }
        return false;
    }

    public synchronized void release() { // TODO does release need to be synchronized?
        numberOfAvailableTokens++;
        System.out.println("token releaesd. " + numberOfAvailableTokens + " tokens available.");
    }
}
