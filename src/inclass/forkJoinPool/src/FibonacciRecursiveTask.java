package inclass.forkJoinPool.src;

import java.util.concurrent.RecursiveTask;

public class FibonacciRecursiveTask extends RecursiveTask<Integer> {
    private final int fibNumber;

    public FibonacciRecursiveTask(int fibNumber) {
        this.fibNumber = fibNumber;
    }

    public Integer computeClassic() {
        if (fibNumber <= 1) {
            return fibNumber;
        }

        FibonacciRecursiveTask f1 = new FibonacciRecursiveTask(fibNumber - 1);
        FibonacciRecursiveTask f2 = new FibonacciRecursiveTask(fibNumber - 2);

        return f1.computeClassic() + f2.computeClassic();
    }

    @Override
    public Integer compute() {
        if (fibNumber <= 1) {
            return fibNumber;
        }

        FibonacciRecursiveTask f1 = new FibonacciRecursiveTask(fibNumber - 1);
        f1.fork();

        FibonacciRecursiveTask f2 = new FibonacciRecursiveTask(fibNumber - 2);

        return f2.compute() + f1.join();
    }


    public static void main(String[] args) {
        final int fibNumber = 30;

        FibonacciRecursiveTask fibClass = new FibonacciRecursiveTask(fibNumber);

        long start = System.currentTimeMillis();
        Integer answer = fibClass.computeClassic();
        System.out.println("classic : " + answer + " in: " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        answer = fibClass.compute();
        System.out.println("recursive task: " + answer + " in: " + (System.currentTimeMillis() - start) + "ms");
    }
}
