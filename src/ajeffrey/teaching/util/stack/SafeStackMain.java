package ajeffrey.teaching.util.stack;

import java.util.Iterator;

public class SafeStackMain {

    public static void main(String[] args) {
        SafeStackImpl safeStack = new SafeStackImpl();


        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                int max = 22;
                int current = 0;
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                    int number = current++ % max;
                    safeStack.push(number);
                    System.out.println("t1 pushed " + number);
                }

            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }

                    System.out.println("t3 popped " + safeStack.pop());

                }

            }
        });

        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                    System.out.println("t4 popped " + safeStack.pop());
                }

            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Iterator iterator;
                while (true) {
                    try {
                        iterator = safeStack.iterator();
                        while (iterator.hasNext()) {
                            Thread.sleep(500);
                            System.out.println("next:" + iterator.next());
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

            }
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
