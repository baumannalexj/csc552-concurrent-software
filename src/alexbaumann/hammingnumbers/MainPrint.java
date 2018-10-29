package alexbaumann.hammingnumbers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainPrint {

    static ExecutorService executor = Executors.newFixedThreadPool(100);
    private static ConcurrentLinkedQueue<String> resultQueue = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {

        int targetHammingNumber = 40;

        CountDownLatch latch = new CountDownLatch(targetHammingNumber);

        OutCopy outCopy = new OutCopy(targetHammingNumber, latch, resultQueue);

        executor.submit(outCopy);

        int currentNumber = 1;
        while (currentNumber <= targetHammingNumber) {
            if (resultQueue.peek() != null) {
                System.out.println(resultQueue.poll());

                currentNumber++;
            } else if (latch.getCount() == 0) {
                break;
            }
        }

        executor.shutdown();
    }
}

class OutCopy implements Runnable {

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

        ConcurrentHashMap<Integer, Integer> divisorIsMultiple2 = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, Integer> divisorIsMultiple3 = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, Integer> divisorIsMultiple5 = new ConcurrentHashMap<>();

        Runnable resultMerger = new InOrderMerge(
                divisorIsMultiple2,
                divisorIsMultiple3,
                divisorIsMultiple5,
                parentLatch,
                resultQueue,
                targetHammingNumber);

        MainPrint.executor.submit(resultMerger);

        int currentNumber = 1;

        while (currentNumber <= targetHammingNumber) {

            MultipleOfChecker multipleOf2 = new MultipleOfChecker(2, currentNumber, divisorIsMultiple2);
            MultipleOfChecker multipleOf3 = new MultipleOfChecker(3, currentNumber, divisorIsMultiple3);
            MultipleOfChecker multipleOf5 = new MultipleOfChecker(5, currentNumber, divisorIsMultiple5);

            MainPrint.executor.submit(multipleOf2);
            MainPrint.executor.submit(multipleOf3);
            MainPrint.executor.submit(multipleOf5);

            currentNumber++;
        }

    }

    private class InOrderMerge implements Runnable {
        ConcurrentHashMap<Integer, Integer> divisorIsMultiple2;
        ConcurrentHashMap<Integer, Integer> divisorIsMultiple3;
        ConcurrentHashMap<Integer, Integer> divisorIsMultiple5;
        private CountDownLatch parentLatch;
        private ConcurrentLinkedQueue<String> resultQueue;
        private int targetNumber;

        public InOrderMerge(ConcurrentHashMap<Integer, Integer> divisorIsMultiple2,
                            ConcurrentHashMap<Integer, Integer> divisorIsMultiple3,
                            ConcurrentHashMap<Integer, Integer> divisorIsMultiple5,
                            CountDownLatch parentLatch,
                            ConcurrentLinkedQueue<String> resultQueue,
                            int targetNumber) {
            this.divisorIsMultiple2 = divisorIsMultiple2;
            this.divisorIsMultiple3 = divisorIsMultiple3;
            this.divisorIsMultiple5 = divisorIsMultiple5;
            this.parentLatch = parentLatch;
            this.resultQueue = resultQueue;
            this.targetNumber = targetNumber;
        }

        @Override
        public void run() {

            int currentNumber = 1;
            while (currentNumber <= targetNumber) {

                Integer exponent2 = divisorIsMultiple2.get(currentNumber);
                Integer exponent3 = divisorIsMultiple3.get(currentNumber);
                Integer exponent5 = divisorIsMultiple5.get(currentNumber);

                if (exponent2 != null
                        && exponent3 != null
                        && exponent5 != null) {

                    if (currentNumber ==
                            Math.pow(2, exponent2) * Math.pow(3, exponent3) * Math.pow(5, exponent5)) {
                        resultQueue.add(String.format("%d \t: 2^%d 3^%d 5^%d", currentNumber, exponent2, exponent3, exponent5));

                    }
                    parentLatch.countDown();
                    currentNumber++;

                }
            }
        }
    }
}


class MultipleOfChecker implements Runnable {

    private int divisor; // 2, 3, 5
    private int potentialHammingNumber;
    private ConcurrentHashMap<Integer, Integer> numberToExponent;

    public MultipleOfChecker(int divisor, int potentialHammingNumber, ConcurrentHashMap<Integer, Integer> numberToExponent) {
        this.divisor = divisor;
        this.potentialHammingNumber = potentialHammingNumber;
        this.numberToExponent = numberToExponent;
    }


    @Override
    public void run() {

        int exponent = 0;
        int reducedNumber = potentialHammingNumber;

        while (reducedNumber % divisor == 0) {
            reducedNumber = reducedNumber / divisor;
            exponent++;
        }

        numberToExponent.put(potentialHammingNumber, exponent);

        //TODO do I need "interrupt = true" ??
    }
}
