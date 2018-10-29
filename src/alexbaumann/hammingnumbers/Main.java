package alexbaumann.hammingnumbers;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static ConcurrentLinkedQueue<Integer> divisorIsMultiple2 = new ConcurrentLinkedQueue<>();
    static ConcurrentLinkedQueue<Integer> divisorIsMultiple3 = new ConcurrentLinkedQueue<>();
    static ConcurrentLinkedQueue<Integer> divisorIsMultiple5 = new ConcurrentLinkedQueue<>();

    static ExecutorService executor = Executors.newFixedThreadPool(100);

    public static void main(String[] args) throws Exception {

        int targetHammingNumber = 40;
        int currentNumber = 1;

        while (currentNumber <= targetHammingNumber) {

            CountDownLatch latch = new CountDownLatch(3);
            MultipleOfChecker multipleOf2 = new MultipleOfChecker(2, currentNumber, divisorIsMultiple2, latch);
            MultipleOfChecker multipleOf3 = new MultipleOfChecker(3, currentNumber, divisorIsMultiple3, latch);
            MultipleOfChecker multipleOf5 = new MultipleOfChecker(5, currentNumber, divisorIsMultiple5, latch);

            executor.submit(multipleOf2);
            executor.submit(multipleOf3);
            executor.submit(multipleOf5);

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new Exception(e.getMessage(), e.getCause());
            }

            Integer exponent2 = divisorIsMultiple2.poll();
            Integer exponent3 = divisorIsMultiple3.poll();
            Integer exponent5 = divisorIsMultiple5.poll();

            if (currentNumber != Math.pow(2, exponent2) * Math.pow(3, exponent3) * Math.pow(5, exponent5)) {
                //not a hamming
                System.out.printf("Number: %d is NOT a Hamming Number", currentNumber);
                System.out.println();

            } else {

                System.out.printf("Number: %d is a hamming number: 2^%d 3^%d 5^%d",
                        currentNumber, exponent2, exponent3, exponent5);
                System.out.println();
            }


            currentNumber++;
        }

        executor.shutdown();

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
