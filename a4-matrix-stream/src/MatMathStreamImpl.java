import java.util.Arrays;
import java.util.stream.IntStream;

public class MatMathStreamImpl implements MatMath {

    @Override
    public void multiply(int[][] A, int[][] B, int[][] result) {

        IntStream.range(0, A.length)
                .parallel().forEach(i -> IntStream.range(0, B[0].length)
                        .parallel().forEach(k -> IntStream.range(0, B.length)
                                .parallel().forEach(j -> result[i][k] += A[i][j] * B[j][k])
                        )
                );

    }

    @Override
    public void add(int[][] A, int[][] B, int[][] result) {
        IntStream.range(0, A.length)
                .parallel().forEach(i -> IntStream.range(0, A[0].length)
                        .parallel().forEach(j -> result[i][j] = A[i][j] + B[i][j])
                );
    }

    @Override
    public void print(int[][] A) {
        for (int i = 0; i < A.length; i++) { // can't stream since it wouldn't be in order
            System.out.println(Arrays.toString(A[i]));
        }
    }
}
