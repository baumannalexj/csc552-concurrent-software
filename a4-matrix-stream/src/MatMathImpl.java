import java.util.Arrays;

public class MatMathImpl implements MatMath {

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
        int numColumns = A[0].length;

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < numColumns; j++) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }
    }

    @Override
    public void print(int[][] A) {
        for (int i = 0; i < A.length; i++) { // can you do this with stream? if stream //-izes, you won't get the order you want
            System.out.println(Arrays.toString(A[i]));
        }
    }
}
