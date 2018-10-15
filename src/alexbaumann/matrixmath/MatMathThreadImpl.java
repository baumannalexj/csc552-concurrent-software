package alexbaumann.matrixmath;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RecursiveTask;

public class MatMathThreadImpl implements MatMath {

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

            //TODO do we want to only start a thread when all are ready?
            matrixMultiply.start();
            latch.await();
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

            if (iLow == iHigh
                    && jLow == jHigh) {

                for (int k = 0; k < A[0].length; k++) {
                    result[iLow][jLow] += A[iLow][k] * B[k][jLow];
                }

            } else {
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

                    if (iLow < iHigh) {
                        iMid = iLow + (iHigh - iLow) / 2; // integer overflow protection
                        upperLowerSplit = true;
                    }

                    if (jLow < jHigh) {
                        jMid = jLow + (jHigh - jLow) / 2;
                        leftRightSplit = true;
                    }


                    if (upperLowerSplit && leftRightSplit) {
                        innerLatch = new CountDownLatch(4);

                        upperLeft = new MatrixMultiply(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                        lowerLeft = new MatrixMultiply(iMid + 1, iHigh, jLow, jMid, A, B, result, innerLatch);
                        upperRight = new MatrixMultiply(iLow, iMid, jMid + 1, jHigh, A, B, result, innerLatch);
                        lowerRight = new MatrixMultiply(iMid + 1, iHigh, jMid + 1, jHigh, A, B, result, innerLatch);

                        upperLeft.run();
                        lowerLeft.run();
                        upperRight.run();
                        lowerRight.run();
                    } else if (upperLowerSplit && !leftRightSplit) {
                        innerLatch = new CountDownLatch(2);

                        upperLeft = new MatrixMultiply(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                        lowerLeft = new MatrixMultiply(iMid + 1, iHigh, jLow, jMid, A, B, result, innerLatch);

                        upperLeft.run();
                        lowerLeft.run();

                    } else { //else if (!upperLowerSplit && leftRightSplit) {
                        innerLatch = new CountDownLatch(2);

                        upperLeft = new MatrixMultiply(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                        upperRight = new MatrixMultiply(iLow, iMid, jMid + 1, jHigh, A, B, result, innerLatch);

                        upperLeft.run();
                        upperRight.run();
                    }

                    innerLatch.await();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            parentLatch.countDown();
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
                    CountDownLatch innerLatch;

                    if (iLow < iHigh) {
                        iMid = (iLow + iHigh) / 2;
                        upperLowerSplit = true;
                    }

                    if (jLow < jHigh) {
                        jMid = (jLow + jHigh) / 2;
                        leftRightSplit = true;
                    }

                    if (upperLowerSplit && leftRightSplit) {
                        innerLatch = new CountDownLatch(4);

                        upperLeft = new MatrixAdd(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                        lowerLeft = new MatrixAdd(iMid + 1, iHigh, jLow, jMid, A, B, result, innerLatch);
                        upperRight = new MatrixAdd(iLow, iMid, jMid + 1, jHigh, A, B, result, innerLatch);
                        lowerRight = new MatrixAdd(iMid + 1, iHigh, jMid + 1, jHigh, A, B, result, innerLatch);

                        upperLeft.run();
                        lowerLeft.run();
                        upperRight.run();
                        lowerRight.run();
                    } else if (upperLowerSplit && !leftRightSplit) {
                        innerLatch = new CountDownLatch(2);

                        upperLeft = new MatrixAdd(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                        lowerLeft = new MatrixAdd(iMid + 1, iHigh, jLow, jMid, A, B, result, innerLatch);

                        upperLeft.run();
                        lowerLeft.run();

                    } else { //else if (!upperLowerSplit && leftRightSplit) {
                        innerLatch = new CountDownLatch(2);

                        upperLeft = new MatrixAdd(iLow, iMid, jLow, jMid, A, B, result, innerLatch);
                        upperRight = new MatrixAdd(iLow, iMid, jMid + 1, jHigh, A, B, result, innerLatch);

                        upperLeft.run();
                        upperRight.run();
                    }

                    innerLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            parentLatch.countDown();
        }
    }
}
