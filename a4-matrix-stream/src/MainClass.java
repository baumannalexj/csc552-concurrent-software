import java.util.Random;

public class MainClass {

    public static void main(String[] args) {
        int[][] A, B, C, D, r, s, t;
        int numRows = 1000;
        int numCols = 1000;

        A = new int[numRows][numCols];
        B = new int[numRows][numCols];
        C = new int[numRows][numCols];
        D = new int[numRows][numCols];
        r = new int[numRows][numCols];
        s = new int[numRows][numCols];
        t = new int[numRows][numCols];


        int min = 1;
        int max = 10;
        Random rand = new Random();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                A[i][j] = rand.nextInt((max - min) + 1) + min;
                B[i][j] = rand.nextInt((max - min) + 1) + min;
                C[i][j] = rand.nextInt((max - min) + 1) + min;
                D[i][j] = rand.nextInt((max - min) + 1) + min;
            }
        }


        //TODO change impl from nested for loops to streams
//        MatMath matMathImpl = new MatMathImpl();
        MatMath matMathImpl = new MatMathStreamImpl();

        System.out.println("Starting now.");
        long start = System.currentTimeMillis();


        matMathImpl.add(A, B, r);

//            System.out.println("\t A");
//        matMathImpl.print(A);
//            System.out.println("\t+B");
//        matMathImpl.print(B);
//            System.out.println("= r:");

//        matMathImpl.print(r);

//        System.out.println("------------");


//            System.out.println("\t r:");
//        matMathImpl.print(r);
//            System.out.println("\txC:");
//        matMathImpl.print(C);
        matMathImpl.multiply(r, C, s); //2x3

//            System.out.println("\ts:");
//        matMathImpl.print(s);


        System.out.println("------------");

//            System.out.println("\t s:");
//        matMathImpl.print(s);
//            System.out.println("\txD:");
//        matMathImpl.print(D);
        matMathImpl.multiply(s, D, t);

//            System.out.println("\tt:");
        matMathImpl.print(t);


        System.out.println("Time: " +
                (System.currentTimeMillis() - start) + " ms");

    }
}
