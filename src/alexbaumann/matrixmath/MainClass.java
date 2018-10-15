package alexbaumann.matrixmath;

import java.util.Random;


/**
 * alexander baumann
 * assignment 6 - matrix addition and multiplication using explicit threads
 */
public class MainClass {

    public static void main(String[] args) {
        int[][] A, B, C, D, r, s, t;
//        int numRows = 1000;
//        int numCols = 1000;
        int numRows = 10;
        int numCols = 10;

        MatMath matMathImpl;


        //TODO uncomment the implementations you with to use
        MatMath matMathSimipleImpl = new MatMathImpl();
//        matMathImpl = new MatMathStreamImpl();
//        matMathImpl = new MatMathForkJoinImpl();
        matMathImpl = new MatMathThreadImpl();


        for (int loop = 0; loop < 5; loop++) {


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


            System.out.println("Starting now.");
            long start = System.currentTimeMillis();


            matMathImpl.add(A, B, r);
//            matMathImpl.print(r);
//            System.out.println("------------");
//            System.out.println("------------");
//
////
            matMathSimipleImpl.multiply(r, C, s); //2x3
            matMathImpl.print(s);



            matMathImpl.multiply(r, C, s); //2x3
            matMathImpl.print(s);
//            System.out.println("------------");


//
//
            matMathImpl.multiply(s, D, t);
//            matMathImpl.print(t);

            System.out.println("------------");

            System.out.println("Time: " +
                    (System.currentTimeMillis() - start) + " ms");


        }


    }
}