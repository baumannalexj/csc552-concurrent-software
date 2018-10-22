package alexbaumann.matrixmath;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatMathExecutorImpl implements MatMath {
    private static final int CUTOFF = 10;
    ExecutorService executor = Executors.newWorkStealingPool(100);

    @Override
    public void print(int[][] A) {
        for (int[] aA : A) {
            System.out.println(Arrays.toString(aA));
        }
    }

    @Override
    public void multiply(int[][] A, int[][] B, int[][] result) {

        CountDownLatch startLatch = new CountDownLatch(1);
        MatrixMultiply multiply = new MatrixMultiply(0, A.length, 0, B[0].length, A, B, result, startLatch);

        executor.submit(multiply);

        try {
            startLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("finished multiply");
    }

    @Override
    public void add(int[][] A, int[][] B, int[][] result) {
        MatrixAdd matrixAdd = new MatrixAdd(0, A.length, 0, A[0].length, A, B, result);
        matrixAdd.start();
        try {
            matrixAdd.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("finished addition");
    }

    class MatrixMultiply extends Thread {

        int iLow;
        int iHigh;
        int jLow;
        int jHigh;
        int[][] A;
        int[][] B;

        int[][] result;
        private CountDownLatch parentLatch;

        public MatrixMultiply(int iLow, int iHigh, int jLow, int jHigh, int[][] a, int[][] b, int[][] result, CountDownLatch parentLatch) {
            this.iLow = iLow;
            this.iHigh = iHigh;
            this.jLow = jLow;
            this.jHigh = jHigh;
            A = a;
            B = b;
            this.result = result;
            this.parentLatch = parentLatch;
        }


        @Override
        public void run() {
            if (iHigh - iLow <= CUTOFF
                    && jHigh - jLow <= CUTOFF) {

                for (int i = iLow; i < iHigh; i++) {
                    for (int j = jLow; j < jHigh; j++) {
                        for (int k = 0; k < A[0].length; k++) {
                            result[i][j] += A[i][k] * B[k][j];
                        }
                    }
                }

            } else {

                try {
                    int iMid = iLow;
                    int jMid = jLow;
                    boolean upperLowerSplit = false;
                    boolean leftRightSplit = false;

                    CountDownLatch innerLatch;

                    if (iLow < iHigh) {
                        iMid = iLow + (iHigh - iLow) / 2; // integer overflow protection
                        upperLowerSplit = true;
                    }

                    if (jLow < jHigh) {
                        jMid = jLow + (jHigh - jLow) / 2;
                        leftRightSplit = true;
                    }


                    if (upperLowerSplit && leftRightSplit) {
                        innerLatch = new CountDownLatch(1);
                        MatrixMultiply upperLeft = new MatrixMultiply(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                        executor.submit(upperLeft);


                        innerLatch.await();
                        innerLatch = new CountDownLatch(1);
                        MatrixMultiply lowerLeft = new MatrixMultiply(iMid + 1, iHigh, jLow, jMid, A, B, result, innerLatch);
                        executor.submit(lowerLeft);
                        innerLatch.await();

                        innerLatch = new CountDownLatch(1);
                        MatrixMultiply upperRight = new MatrixMultiply(iLow, iMid, jMid + 1, jHigh, A, B, result, innerLatch);
                        executor.submit(upperRight);

                        innerLatch.await();
                        innerLatch = new CountDownLatch(1);

                        MatrixMultiply lowerRight = new MatrixMultiply(iMid + 1, iHigh, jMid + 1, jHigh, A, B, result, innerLatch);
                        executor.submit(lowerRight);


                    } else if (upperLowerSplit && !leftRightSplit) {
                        innerLatch = new CountDownLatch(1);
                        MatrixMultiply upperLeft = new MatrixMultiply(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                        executor.submit(upperLeft);

                        innerLatch.await();
                        innerLatch = new CountDownLatch(1);

                        MatrixMultiply lowerLeft = new MatrixMultiply(iMid + 1, iHigh, jLow, jMid, A, B, result, innerLatch);
                        executor.submit(lowerLeft);

                    } else { //(!upperLowerSplit && leftRightSplit) {
                        innerLatch = new CountDownLatch(1);
                        MatrixMultiply upperLeft = new MatrixMultiply(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                        executor.submit(upperLeft);

                        innerLatch.await();
                        innerLatch = new CountDownLatch(1);

                        MatrixMultiply upperRight = new MatrixMultiply(iLow, iMid, jMid + 1, jHigh, A, B, result, innerLatch);
                        executor.submit(upperRight);
                    }
                    innerLatch.await();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            parentLatch.countDown();
            return;
        }
    }

    class MatrixAdd extends Thread {

        int iLow;
        int iHigh;
        int jLow;
        int jHigh;
        int[][] A;
        int[][] B;

        int[][] result;

        public MatrixAdd(int iLow, int iHigh, int jLow, int jHigh, int[][] a, int[][] b, int[][] result) {
            this.iLow = iLow;
            this.iHigh = iHigh;
            this.jLow = jLow;
            this.jHigh = jHigh;
            A = a;
            B = b;
            this.result = result;
        }

        @Override
        public void run() {
            if (iLow == iHigh
                    && jLow == jHigh) {

                result[iLow][jLow] = A[iLow][jLow] + B[iLow][jLow];

            } else {

                try {
                    int iMid = iLow;
                    int jMid = jLow;
                    boolean upperLowerSplit = false;
                    boolean leftRightSplit = false;
                    MatrixAdd upperLeft;
                    MatrixAdd lowerLeft;
                    MatrixAdd upperRight;
                    MatrixAdd lowerRight;

                    if (iLow < iHigh) {
                        iMid = iLow + (iHigh - iLow) / 2; // integer overflow protection
                        upperLowerSplit = true;
                    }

                    if (jLow < jHigh) {
                        jMid = jLow + (jHigh - jLow) / 2;
                        leftRightSplit = true;
                    }


                    upperLeft = new MatrixAdd(iLow, iMid, jLow, jMid, A, B, result);
                    lowerLeft = new MatrixAdd(iMid + 1, iHigh, jLow, jMid, A, B, result);
                    upperRight = new MatrixAdd(iLow, iMid, jMid + 1, jHigh, A, B, result);
                    lowerRight = new MatrixAdd(iMid + 1, iHigh, jMid + 1, jHigh, A, B, result);

                    if (upperLowerSplit && leftRightSplit) {

                        executor.submit(upperLeft);
                        executor.submit(lowerLeft);
                        executor.submit(upperRight);
                        executor.submit(lowerRight);

                        upperLeft.join();
                        lowerLeft.join();
                        upperRight.join();
                        lowerRight.join();

                    } else if (upperLowerSplit && !leftRightSplit) {
                        executor.submit(upperLeft);
                        executor.submit(lowerLeft);

                        upperLeft.join();
                        lowerLeft.join();

                    } else { //(!upperLowerSplit && leftRightSplit) {
                        executor.submit(upperLeft);
                        executor.submit(upperRight);

                        upperLeft.join();
                        upperRight.join();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            return;
        }
    }

}
