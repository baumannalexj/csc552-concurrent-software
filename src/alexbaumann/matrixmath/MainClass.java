package alexbaumann.matrixmath;

import java.util.Random;


/**
 * alexander baumann
 * assignment 7 - matrix addition and multiplication using task executor
 */
public class MainClass {

    public static void main(String[] args) {
        int[][] A, B, C, D, r, s, t;
//        int numRows = 1000;
//        int numCols = 1000;
        int numRows = 1000;
        int numCols = 1000;

        MatMath matMathImpl;


        //TODO uncomment the implementations you wish to use
//        MatMath matMathSimpleImpl = new MatMathImpl();
//        matMathImpl = new MatMathImpl();
//        matMathImpl = new MatMathStreamImpl();
//        matMathImpl = new MatMathForkJoinImpl();
//        matMathImpl = new MatMathThreadImpl();
//        matMathImpl = new MatMathLatchImpl();
        matMathImpl = new MatMathExecutorImpl();


        A = new int[numRows][numCols];
        B = new int[numRows][numCols];
        C = new int[numRows][numCols];
        D = new int[numRows][numCols];
        r = new int[numRows][numCols];
        s = new int[numRows][numCols];
        t = new int[numRows][numCols];

        System.out.println("initializing...");
        int min = 1;
        int max = 10;

        Random rand = new Random();
        for (int loop = 0; loop < 5; loop++) {

            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    A[i][j] = rand.nextInt((max - min) + 1) + min;
                    B[i][j] = rand.nextInt((max - min) + 1) + min;
                    C[i][j] = rand.nextInt((max - min) + 1) + min;
                    D[i][j] = rand.nextInt((max - min) + 1) + min;
                }
            }


            System.out.println("Starting now...");
            long start = System.currentTimeMillis();


//            matMathSimpleImpl.add(A, B, r);
//            matMathSimpleImpl.print(r);
//            System.out.println("------------");

//            r = new int[numRows][numCols];

            matMathImpl.add(A, B, r);
//            matMathImpl.print(r);
//            System.out.println("------------");
//
////
//            matMathSimpleImpl.multiply(r, C, s);
//            matMathSimpleImpl.print(s);


//            System.out.println("------------");
//            s = new int[numRows][numCols];

            matMathImpl.multiply(r, C, s);
//            matMathImpl.print(s);
//            System.out.println("------------");


//
            matMathImpl.multiply(s, D, t);
//            matMathImpl.print(t);


            System.out.println("Time: " +
                    (System.currentTimeMillis() - start) + " ms");

            System.out.println("------------");

        }

        System.out.println("all done");
    }
}
