import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class MatMathForkJoinImpl implements MatMath {

    @Override
    public void print(int[][] A) {
        for (int i = 0; i < A.length; i++) {
            System.out.println(Arrays.toString(A[i]));
        }
    }

    @Override

    public void multiply(int[][] A, int[][] B, int[][] result) {
        MatrixMultiply matrixAdd = new MatrixMultiply(0, A.length - 1, 0, B[0].length - 1, A, B, result);
        matrixAdd.fork();
        matrixAdd.join();

    }

    @Override
    public void add(int[][] A, int[][] B, int[][] result) {
        MatrixAdd matrixAdd = new MatrixAdd(0, A.length - 1, 0, A[0].length - 1, A, B, result);
        matrixAdd.fork();
        matrixAdd.join();
    }

    class MatrixMultiply extends RecursiveTask<Void> {

        int iLow;
        int iHigh;
        int jLow;
        int jHigh;
        int[][] A;
        int[][] B;

        int[][] result;

        public MatrixMultiply(int iLow, int iHigh, int jLow, int jHigh, int[][] a, int[][] b, int[][] result) {
            this.iLow = iLow;
            this.iHigh = iHigh;
            this.jLow = jLow;
            this.jHigh = jHigh;
            A = a;
            B = b;
            this.result = result;
        }


        protected Void compute() {
            if (iLow == iHigh
                    && jLow == jHigh) {

//                System.out.printf("i %d j %d", iLow, jLow);
//                System.out.println();
                for (int k = 0; k < A[0].length; k++) {
                    result[iLow][jLow] += A[iLow][k] * B[k][jLow];
                }


            } else {
                int iMid = iLow;
                int jMid = jLow;
                boolean upperLowerSplit = false;
                boolean leftRightSplit = false;

                if (iLow < iHigh) {
                    iMid = (iLow + iHigh) / 2;
                    upperLowerSplit = true;
                }

                if (jLow < jHigh) {
                    jMid = (jLow + jHigh) / 2;
                    leftRightSplit = true;
                }

                MatrixMultiply upperLeft = new MatrixMultiply(iLow, iMid, jLow, jMid, A, B, result);
                MatrixMultiply lowerLeft = new MatrixMultiply(iMid + 1, iHigh, jLow, jMid, A, B, result);
                MatrixMultiply upperRight = new MatrixMultiply(iLow, iMid, jMid + 1, jHigh, A, B, result);
                MatrixMultiply lowerRight = new MatrixMultiply(iMid + 1, iHigh, jMid + 1, jHigh, A, B, result);


                if (upperLowerSplit && leftRightSplit) {
                    upperLeft.fork();
                    lowerLeft.fork();
                    upperRight.fork();
                    lowerRight.fork();

                    upperLeft.join();
                    lowerLeft.join();
                    upperRight.join();
                    lowerRight.join();

                } else if (upperLowerSplit && !leftRightSplit) {

                    upperLeft.fork();
                    lowerLeft.fork();

                    upperLeft.join();
                    lowerLeft.join();

                } else if (!upperLowerSplit && leftRightSplit) {
                    upperLeft.fork();
                    upperRight.fork();

                    upperLeft.join();
                    upperRight.join();
                }
            }

            return null;
        }
    }

    class MatrixAdd extends RecursiveTask<Void> {

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


        protected Void compute() {
            if (iLow == iHigh
                    && jLow == jHigh) {

                result[iLow][jLow] = A[iLow][jLow] + B[iLow][jLow];

            } else {
                int iMid = iLow;
                int jMid = jLow;
                boolean upperLowerSplit = false;
                boolean leftRightSplit = false;

                if (iLow < iHigh) {
                    iMid = (iLow + iHigh) / 2;
                    upperLowerSplit = true;
                }

                if (jLow < jHigh) {
                    jMid = (jLow + jHigh) / 2;
                    leftRightSplit = true;
                }

                MatrixAdd upperLeft = new MatrixAdd(iLow, iMid, jLow, jMid, A, B, result);
                MatrixAdd lowerLeft = new MatrixAdd(iMid + 1, iHigh, jLow, jMid, A, B, result);
                MatrixAdd upperRight = new MatrixAdd(iLow, iMid, jMid + 1, jHigh, A, B, result);
                MatrixAdd lowerRight = new MatrixAdd(iMid + 1, iHigh, jMid + 1, jHigh, A, B, result);


                if (upperLowerSplit && leftRightSplit) {
                    upperLeft.fork();
                    lowerLeft.fork();
                    upperRight.fork();
                    lowerRight.fork();

                    upperLeft.join();
                    lowerLeft.join();
                    upperRight.join();
                    lowerRight.join();

                } else if (upperLowerSplit && !leftRightSplit) {

                    upperLeft.fork();
                    lowerLeft.fork();

                    upperLeft.join();
                    lowerLeft.join();

                } else if (!upperLowerSplit && leftRightSplit) {
                    upperLeft.fork();
                    upperRight.fork();

                    upperLeft.join();
                    upperRight.join();
                }
            }

            return null;
        }
    }
}
