public interface MatMath{
    void multiply(int[][] A, int[][]B, int[][]result);  // multiply A and B into result
    void add(int[][]A, int[][]B, int[][]result);        // add A and B into result
    void print(int[][]A);                          // pretty print A
}