package inclass.forkJoinPool.src;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class SortTaskRecursiveAction extends RecursiveAction {

    private static final int THRESHOLD = 1000;
    final long[] array;
    final int lo, hi;

    public SortTaskRecursiveAction(long[] array, int lo, int hi) {
        this.array = array;
        this.lo = lo;
        this.hi = hi;
    }

    public SortTaskRecursiveAction(long[] array ) {
        this(array, 0, array.length);
    }

    @Override
    protected void compute() {

        if (hi - lo < THRESHOLD) {
            sortSequentially(lo, hi);
        } else {
            int mid = (lo + hi) >>> 1;

            invokeAll(new SortTaskRecursiveAction(array, lo, mid),
                    new SortTaskRecursiveAction(array, mid, hi));

            /**
             * NOTE: invokeAll starts all, and waits for all to finish
             *
             * is same idea as:
             *      f1.fork();
             *      f2.fork();
             *
             *      f1.join();
             *      f2.join();
             * */

            merge(lo, mid, hi);
        }

    }

    private void sortSequentially(int lo, int hi) {
        Arrays.sort(array, lo, hi);
    }

    private void merge(int lo, int mid, int hi) {
        long[] buf = Arrays.copyOfRange(array, lo, mid);
        for (int i = 0, j = lo, k = mid; i < buf.length; j++)
            array[j] = (k == hi || buf[i] < array[k])
                    ? buf[i++]
                    : array[k++];

    }
}
