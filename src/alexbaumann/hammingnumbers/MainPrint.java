package alexbaumann.hammingnumbers;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainPrint {

    //TODO could make more concurrent with hashmap
    // numberTo2Exponent {  (1,0), (2,1), (3,0) ... (40, 3) }
    // numberTo3Exponent {  (1,0), (2,0), (3,1) ... (40, 0) }
    // numberTo5Exponent {  (1,0), (2,0), (3,0) ... (40, 1) }

    private static ConcurrentLinkedQueue<String> resultQueue = new ConcurrentLinkedQueue<>();

    static ExecutorService executor = Executors.newFixedThreadPool(100);

    public static void main(String[] args) {

        int targetHammingNumber = 40;

        CountDownLatch latch = new CountDownLatch(1);

        OutCopy outCopy = new OutCopy(targetHammingNumber, latch, resultQueue);

        executor.submit(outCopy);

        int startNumber = 1;
        while (latch.getCount() != 0) {
            if (resultQueue.peek() != null) {
                System.out.println(resultQueue.poll());
            }
        }

        executor.shutdown();
    }
}

class OutCopy implements Runnable {

    private ConcurrentLinkedQueue<Integer> divisorIsMultiple2 = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Integer> divisorIsMultiple3 = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Integer> divisorIsMultiple5 = new ConcurrentLinkedQueue<>();

    private int targetHammingNumber;
    private CountDownLatch parentLatch;
    private ConcurrentLinkedQueue<String> resultQueue;

    OutCopy(int targetHammingNumber, CountDownLatch parentLatch, ConcurrentLinkedQueue<String> resultQueue) {
        this.targetHammingNumber = targetHammingNumber;
        this.parentLatch = parentLatch;
        this.resultQueue = resultQueue;
    }


    @Override
    public void run() {
        int currentNumber = 1;

        while (currentNumber <= targetHammingNumber) {

            CountDownLatch latch = new CountDownLatch(3);
            MultipleOfChecker multipleOf2 = new MultipleOfChecker(2, currentNumber, divisorIsMultiple2, latch);
            MultipleOfChecker multipleOf3 = new MultipleOfChecker(3, currentNumber, divisorIsMultiple3, latch);
            MultipleOfChecker multipleOf5 = new MultipleOfChecker(5, currentNumber, divisorIsMultiple5, latch);

            MainPrint.executor.submit(multipleOf2);
            MainPrint.executor.submit(multipleOf3);
            MainPrint.executor.submit(multipleOf5);

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Integer exponent2 = divisorIsMultiple2.poll();
            Integer exponent3 = divisorIsMultiple3.poll();
            Integer exponent5 = divisorIsMultiple5.poll();

            if (currentNumber == Math.pow(2, exponent2) * Math.pow(3, exponent3) * Math.pow(5, exponent5)) {
                resultQueue.add(String.format("%d \t: 2^%d 3^%d 5^%d", currentNumber, exponent2, exponent3, exponent5));
            }

            currentNumber++;
        }

        parentLatch.countDown();
    }

}


class MultipleOfChecker implements Runnable {

    private int divisor; // 2, 3, 5
    private int potentialHammingNumber;
    private ConcurrentLinkedQueue<Integer> resultQueue;
    private CountDownLatch parentLatch;

    public MultipleOfChecker(int divisor, int potentialHammingNumber, ConcurrentLinkedQueue<Integer> resultQueue, CountDownLatch parentLatch) {
        this.divisor = divisor;
        this.potentialHammingNumber = potentialHammingNumber;
        this.resultQueue = resultQueue;
        this.parentLatch = parentLatch;
    }


    @Override
    public void run() {

        int exponent = 0;
        int reducedNumber = potentialHammingNumber;

        while (reducedNumber % divisor == 0) {
            reducedNumber = reducedNumber / divisor;
            exponent++;
        }

        resultQueue.add(exponent);
        parentLatch.countDown();

        //TODO do I need "interrupt = true" ??
    }
}
