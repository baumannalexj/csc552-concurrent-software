import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class MatMathForkJoinImpl implements MatMath {


    @Override

    public void multiply(int[][] A, int[][] B, int[][] result) {
        for (int i = 0; i < A.length; i++) {
            for (int k = 0; k < B[0].length; k++) {
                for (int j = 0; j < B.length; j++) {
                    result[i][k] += A[i][j] * B[j][k];
                }
            }
        }

    }

    @Override
    public void add(int[][] A, int[][] B, int[][] result) {
        MatrixAdd matrixAdd = new MatrixAdd(0, A.length-1, 0, A[0].length-1, A, B, result);
        matrixAdd.fork();
        matrixAdd.join();
    }

    @Override
    public void print(int[][] A) {
        for (int i = 0; i < A.length; i++) {
            System.out.println(Arrays.toString(A[i]));
        }
    }
//
//    class MatrixMultiply extends RecursiveTask<Integer> {
//        int k;
//        int A;
//        int B;
//
//        MatrixMultiply(int k, int A, int B) {
//            this.k = k;
//            this.A = A;
//            this.B = B;
//
//        }
//
//        @Override
//        protected Integer compute() {
//            return A * B;
//        }
//    }

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
                MatrixAdd lowerLeft = new MatrixAdd(iMid+1, iHigh, jLow, jMid, A, B, result);
                MatrixAdd upperRight = new MatrixAdd(iLow, iMid, jMid+1, jHigh, A, B, result);
                MatrixAdd lowerRight = new MatrixAdd(iMid+1, iHigh, jMid+1, jHigh, A, B, result);


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
