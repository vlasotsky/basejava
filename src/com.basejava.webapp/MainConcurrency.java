package com.basejava.webapp;

import java.util.ArrayList;
import java.util.List;

public class MainConcurrency {
    public static final int THREADS_NUMBER = 10000;
    private int counter;
    private static final Object LOCK = new Object();

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());

        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + ", " + getState());
                throw new IllegalStateException();
            }
        };
        thread0.start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState());
            }

            private void inc() {
                synchronized (this) {
//                    counter++;
                }
            }

        }).start();

        System.out.println(thread0.getState());

        final MainConcurrency mainConcurrency = new MainConcurrency();
        List<Thread> threads = new ArrayList<>(THREADS_NUMBER);

        for (int i = 0; i < THREADS_NUMBER; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.inc();
                }
            });
            thread.start();
            threads.add(thread);
        }

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(mainConcurrency.counter);
    }

    private synchronized void inc() {
//        synchronized (this) {
//        synchronized (MainConcurrency.class) {
        counter++;
//                wait();
//                readFile
//                ...
//        }
    }
}

class DeadLockTest {
    private final CustomObject objectOne;
    private final CustomObject objectTwo;

    DeadLockTest() {
        this.objectOne = new CustomObject();
        this.objectTwo = new CustomObject();
    }

    public static void main(String[] args) {
        DeadLockTest deadLockTest = new DeadLockTest();

        Thread thread1 = new Thread(() -> deadLockTest.objectOne.methodOne(deadLockTest.objectTwo));

        Thread thread2 = new Thread(() -> deadLockTest.objectTwo.methodOne(deadLockTest.objectOne));

        thread1.start();
        thread2.start();

        System.out.println("Test is running");
    }


    class CustomObject {

        public synchronized void methodOne(CustomObject object) {
            System.out.println("MethodOne " + Thread.currentThread().getName());
            object.methodTwo();
        }

        public synchronized void methodTwo() {
            try {
                System.out.println("MethodTwo " + Thread.currentThread().getName());
                Thread.sleep(1000);

                System.out.println("Test is finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}