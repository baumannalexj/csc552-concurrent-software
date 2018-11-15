package alexbaumann.hammingnumbers;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HammingGeneratorMain {

    static ExecutorService executor = Executors.newFixedThreadPool(100);


    public static void main(String[] args) {

        /** //////////////////////////////
         *
         *  initialization:
         *
         *  targetNumber = number you'd like to generate Hamming numbers up to
         *
         * */

        int targetNumber;

        switch (args.length) {
            case 1 :
                targetNumber = Integer.parseInt(args[0]);
                break;
            default:
                targetNumber = 40;
        }


        ConcurrentLinkedQueue<Integer> inputQueue = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Integer> outputQueue = new ConcurrentLinkedQueue<>();

        ConcurrentLinkedQueue<Integer> inputMultiply2 = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Integer> inputMultiply3 = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Integer> inputMultiply5 = new ConcurrentLinkedQueue<>();

        ConcurrentLinkedQueue<Integer> outputMultiply2 = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Integer> outputMultiply3 = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Integer> outputMultiply5 = new ConcurrentLinkedQueue<>();


        inputQueue.add(1);
        OutCopyWorker outCopyWorker = new OutCopyWorker(inputQueue, outputQueue, inputMultiply2, inputMultiply3, inputMultiply5);
        InOrderMerger inOrderMerger = new InOrderMerger(inputQueue, outputMultiply2, outputMultiply3, outputMultiply5);

        MultiplyWorker multiply2Worker = new MultiplyWorker(2, inputMultiply2, outputMultiply2);
        MultiplyWorker multiply3Worker = new MultiplyWorker(3, inputMultiply3, outputMultiply3);
        MultiplyWorker multiply5Worker = new MultiplyWorker(5, inputMultiply5, outputMultiply5);


        CountDownLatch countDownLatch = new CountDownLatch(1);
        PrintWorker printWorker = new PrintWorker(outputQueue, targetNumber, countDownLatch);


        executor.submit(multiply2Worker);
        executor.submit(multiply3Worker);
        executor.submit(multiply5Worker);
        executor.submit(inOrderMerger);
        executor.submit(outCopyWorker);
        executor.submit(printWorker);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("done -- forcing pool shutdown");
            executor.shutdownNow();
        }
    }
}


class PrintWorker implements Runnable {

    private final ConcurrentLinkedQueue<Integer> outputQueue;
    private final int targetNumber;
    private final CountDownLatch countDownLatch;

    public PrintWorker(ConcurrentLinkedQueue<Integer> outputQueue, int targetNumber, CountDownLatch countDownLatch) {
        this.outputQueue = outputQueue;
        this.targetNumber = targetNumber;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            if (outputQueue.peek() != null) {
                Integer hammingResult = outputQueue.poll();
                if (hammingResult > targetNumber) {
                    Thread.currentThread().interrupt();
                    System.out.println("target reached: " + targetNumber);
                } else {
                    System.out.println(hammingResult);
                }
            }
        }

        countDownLatch.countDown();
    }
}


class OutCopyWorker implements Runnable {

    private ConcurrentLinkedQueue<Integer> inputQueue;
    private ConcurrentLinkedQueue<Integer> outputQueue;
    private ConcurrentLinkedQueue<Integer> inputMultiply2;
    private ConcurrentLinkedQueue<Integer> inputMultiply3;
    private ConcurrentLinkedQueue<Integer> inputMultiply5;


    public OutCopyWorker(ConcurrentLinkedQueue<Integer> inputQueue,
                         ConcurrentLinkedQueue<Integer> outputQueue,
                         ConcurrentLinkedQueue<Integer> inputMultiply2,
                         ConcurrentLinkedQueue<Integer> inputMultiply3,
                         ConcurrentLinkedQueue<Integer> inputMultiply5) {

        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.inputMultiply2 = inputMultiply2;
        this.inputMultiply3 = inputMultiply3;
        this.inputMultiply5 = inputMultiply5;
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            if (inputQueue.peek() != null) {
                int nextInput = inputQueue.poll();

                outputQueue.add(nextInput);

                inputMultiply2.add(nextInput);
                inputMultiply3.add(nextInput);
                inputMultiply5.add(nextInput);

            }
        }
    }
}

class InOrderMerger implements Runnable {


    private final ConcurrentLinkedQueue<Integer> inputQueue;
    private final ConcurrentLinkedQueue<Integer> outputMultiply2;
    private final ConcurrentLinkedQueue<Integer> outputMultiply3;
    private final ConcurrentLinkedQueue<Integer> outputMultiply5;

    public InOrderMerger(ConcurrentLinkedQueue<Integer> inputQueue,
                         ConcurrentLinkedQueue<Integer> outputMultiply2,
                         ConcurrentLinkedQueue<Integer> outputMultiply3,
                         ConcurrentLinkedQueue<Integer> outputMultiply5) {
        this.inputQueue = inputQueue;
        this.outputMultiply2 = outputMultiply2;
        this.outputMultiply3 = outputMultiply3;
        this.outputMultiply5 = outputMultiply5;
    }

    @Override
    public void run() {

        int mult2result = -1;
        int mult3result = -1;
        int mult5result = -1;

        while (!Thread.currentThread().isInterrupted()) {

            if (mult2result < 0 && outputMultiply2.peek() != null) {
                mult2result = outputMultiply2.poll();
            }

            if (mult3result < 0 && outputMultiply3.peek() != null) {
                mult3result = outputMultiply3.poll();
            }

            if (mult5result < 0 && outputMultiply5.peek() != null) {
                mult5result = outputMultiply5.poll();
            }

            if (mult2result > 0
                    && mult3result > 0
                    && mult5result > 0) {

                int minOf2And3 = Math.min(mult2result, mult3result);
                int absoluteMin = Math.min(minOf2And3, mult5result);

                inputQueue.add(absoluteMin);

                if (mult2result == absoluteMin) {
                    mult2result = -1;
                }

                if (mult3result == absoluteMin) {
                    mult3result = -1;
                }

                if (mult5result == absoluteMin) {
                    mult5result = -1;
                }
            }


        }

    }
}


class MultiplyWorker implements Runnable {


    private final int multiplier;
    private final ConcurrentLinkedQueue<Integer> inputMultiplyQueue;
    private final ConcurrentLinkedQueue<Integer> outputMultiplyQueue;

    public MultiplyWorker(int multiplier, ConcurrentLinkedQueue<Integer> inputMultiplyQueue, ConcurrentLinkedQueue<Integer> outputMultiplyQueue) {

        this.multiplier = multiplier;
        this.inputMultiplyQueue = inputMultiplyQueue;
        this.outputMultiplyQueue = outputMultiplyQueue;
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            if (inputMultiplyQueue.peek() != null) {
                int mutliplyResult = multiplier * inputMultiplyQueue.poll();
                outputMultiplyQueue.add(mutliplyResult);
            }
        }


    }
}