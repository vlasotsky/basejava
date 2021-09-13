package com.basejava.webapp;

public class MainDeadlock {
    public static void main(String[] args) {
        Object firstObject = new Object();
        Object secondObject = new Object();

        new Thread(() -> objectLocking(firstObject, secondObject)).start();
        new Thread(() -> objectLocking(secondObject, firstObject)).start();
    }

    private static void objectLocking(Object firstObject, Object secondObject) {
        synchronized (secondObject) {
            System.out.println(Thread.currentThread().getName() + " captured firstObject's monitor");

            System.out.println("Trying to capture secondObject's monitor");

            synchronized (firstObject) {
                System.out.println(Thread.currentThread().getName() + " captured secondObject's monitor");
            }
        }
    }
}

