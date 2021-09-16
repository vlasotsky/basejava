package com.basejava.webapp;

public class MainDeadlock {
    public static void main(String[] args) {
        Object firstObject = new Object();
        Object secondObject = new Object();

        new Thread(() -> objectLocking(firstObject, secondObject)).start();
        new Thread(() -> objectLocking(secondObject, firstObject)).start();
    }

    private static void objectLocking(Object firstObject, Object secondObject) {
        synchronized (firstObject) {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " captured " + firstObject + "'s monitor");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(threadName + " is waiting to capture" + secondObject + "'s monitor");
            synchronized (secondObject) {
                System.out.println(threadName + " captured " + secondObject + "'s monitor");
            }
        }
    }
}

