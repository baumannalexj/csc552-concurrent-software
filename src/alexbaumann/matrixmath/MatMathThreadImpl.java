package alexbaumann.matrixmath;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MatMathThreadImpl implements MatMath {

    public final int CUTOFF_DIMENSION;
    ExecutorService executor = Executors.newFixedThreadPool(10000);

    public MatMathThreadImpl(int numCols) {
        CUTOFF_DIMENSION = numCols/2;
    }

    @Override
    public void print(int[][] A) {
        for (int[] aA : A) {
            System.out.println(Arrays.toString(aA));
        }
    }

    @Override

    public void multiply(int[][] A, int[][] B, int[][] result) {

        CountDownLatch latch = new CountDownLatch(1);

        MatrixMultiply matrixMultiply = new MatrixMultiply(0, A.length - 1, 0, B[0].length - 1, A, B, result, latch);
        try {

            matrixMultiply.start();
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            if (iHigh - iLow <= CUTOFF_DIMENSION
                    && jHigh - jLow <= CUTOFF_DIMENSION) { // && or || ?

                for (int i = 0; i < A.length; i++) {
                    for (int j = 0; j < B[0].length; j++) {
                        for (int k = 0; k < B.length; k++) {
                            result[i][j] += A[i][k] * B[k][j];
                        }
                    }
                }

            } else {
                doDynamicMatrixMultiplySplit(iLow, jLow, iHigh, jHigh, A, B, result);
            }

            parentLatch.countDown();
        }
    }

    private void doDynamicMatrixMultiplySplit(int iLow, int jLow, int iHigh, int jHigh, int[][] A, int[][] B, int[][] result) {
        System.out.println("matrix mult");
        try {
            int iMid = iLow;
            int jMid = jLow;
            boolean upperLowerSplit = false;
            boolean leftRightSplit = false;


            MatrixMultiply upperLeft;
            MatrixMultiply lowerLeft;
            MatrixMultiply upperRight;
            MatrixMultiply lowerRight;

            CountDownLatch innerLatch;

            if (iHigh - iLow > CUTOFF_DIMENSION) {
                iMid = iLow + (iHigh - iLow) / 2; // integer overflow protection
                upperLowerSplit = true;
            }

            if (jHigh - jLow > CUTOFF_DIMENSION) {
                jMid = jLow + (jHigh - jLow) / 2;
                leftRightSplit = true;
            }


            if (upperLowerSplit && leftRightSplit) {
                innerLatch = new CountDownLatch(4);

                upperLeft = new MatrixMultiply(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                lowerLeft = new MatrixMultiply(iMid + 1, iHigh, jLow, jMid, A, B, result, innerLatch);

                upperRight = new MatrixMultiply(iLow, iMid, jMid + 1, jHigh, A, B, result, innerLatch);
                lowerRight = new MatrixMultiply(iMid + 1, iHigh, jMid + 1, jHigh, A, B, result, innerLatch);



                executor.submit(upperLeft);
                lowerLeft.run();
//                executor.submit(lowerLeft);
//                upperRight.run();
                executor.submit(upperRight);
                executor.submit(lowerRight);
//                lowerRight.run();

            } else if (upperLowerSplit && !leftRightSplit) {
                innerLatch = new CountDownLatch(2);

                upperLeft = new MatrixMultiply(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                lowerLeft = new MatrixMultiply(iMid + 1, iHigh, jLow, jMid, A, B, result, innerLatch);

                executor.submit(upperLeft);
//                executor.submit(lowerLeft);
                lowerLeft.run();

            } else { //else if (!upperLowerSplit && leftRightSplit) {
                innerLatch = new CountDownLatch(2);

                upperLeft = new MatrixMultiply(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                upperRight = new MatrixMultiply(iLow, iMid, jMid + 1, jHigh, A, B, result, innerLatch);

                executor.submit(upperLeft);
//                executor.submit(upperRight);
                upperRight.run();
            }

            innerLatch.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(int[][] A, int[][] B, int[][] result) {
        CountDownLatch latch = new CountDownLatch(1);
        MatrixAdd matrixAdd = new MatrixAdd(0, A.length - 1, 0, A[0].length - 1, A, B, result, latch);

        try {
            matrixAdd.start();
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("add finished");
    }


    class MatrixAdd extends Thread {

        int iLow;
        int iHigh;
        int jLow;
        int jHigh;
        int[][] A;
        int[][] B;

        int[][] result;

        private CountDownLatch parentLatch;

        public MatrixAdd(int iLow, int iHigh, int jLow, int jHigh, int[][] a, int[][] b, int[][] result, CountDownLatch parentLatch) {
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
            if (iHigh - iLow <= CUTOFF_DIMENSION
                    && jHigh - jLow <= CUTOFF_DIMENSION) { // && or || ?

                MatMath matMath = new MatMathImpl();
                matMath.add(A, B, result);


            } else {
                doDynamicMatrixAdditionSplit(iLow, jLow, iHigh, jHigh, A, B, result);
            }

            parentLatch.countDown();
        }
    }

    private void doDynamicMatrixAdditionSplit(int iLow, int jLow, int iHigh, int jHigh, int[][] A, int[][] B, int[][] result) {
        System.out.println("matrix add");

        try {
            int iMid = iLow;
            int jMid = jLow;
            boolean upperLowerSplit = false;
            boolean leftRightSplit = false;


            MatrixAdd upperLeft;
            MatrixAdd lowerLeft;
            MatrixAdd upperRight;
            MatrixAdd lowerRight;

            CountDownLatch innerLatch;

            if (iHigh - iLow > CUTOFF_DIMENSION) {
                iMid = iLow + (iHigh - iLow) / 2; // integer overflow protection
                upperLowerSplit = true;
            }

            if (jHigh - jLow > CUTOFF_DIMENSION) {
                jMid = jLow + (jHigh - jLow) / 2;
                leftRightSplit = true;
            }

            if (upperLowerSplit && leftRightSplit) {
                innerLatch = new CountDownLatch(4);

                upperLeft = new MatrixAdd(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                lowerLeft = new MatrixAdd(iMid + 1, iHigh, jLow, jMid, A, B, result, innerLatch);
                upperRight = new MatrixAdd(iLow, iMid, jMid + 1, jHigh, A, B, result, innerLatch);
                lowerRight = new MatrixAdd(iMid + 1, iHigh, jMid + 1, jHigh, A, B, result, innerLatch);


                executor.submit(upperLeft);
                lowerLeft.run();
//                executor.submit(lowerLeft);
//                upperRight.run();
                executor.submit(upperRight);
                executor.submit(lowerRight);
//                lowerRight.run();

            } else if (upperLowerSplit && !leftRightSplit) {
                innerLatch = new CountDownLatch(2);

                upperLeft = new MatrixAdd(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                lowerLeft = new MatrixAdd(iMid + 1, iHigh, jLow, jMid, A, B, result, innerLatch);

                executor.submit(upperLeft);
//                executor.submit(lowerLeft);
                lowerLeft.run();

            } else { //else if (!upperLowerSplit && leftRightSplit) {
                innerLatch = new CountDownLatch(2);

                upperLeft = new MatrixAdd(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                upperRight = new MatrixAdd(iLow, iMid, jMid + 1, jHigh, A, B, result, innerLatch);


                executor.submit(upperLeft);
//                executor.submit(upperRight);
                upperRight.run();
            }

            innerLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
