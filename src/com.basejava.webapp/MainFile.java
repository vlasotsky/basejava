package com.basejava.webapp;

import java.io.File;
import java.io.IOException;

public class MainFile {
    public static void main(String[] args) throws IOException {
        String filePath = ".\\.gitignore";
        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }
        File dir = new File(".\\com.basejava.webapp");
//        System.out.println(dir);
//        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
//                System.out.println(name);
            }
        }
        String rootPath = ".\\com.basejava.webapp";
        File newFile = new File(".\\com.basejava.webapp");

        recursiveFileReader(".\\com.basejava.webapp");

    }

    private static void recursiveFileReader(String path) throws IOException {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if(files != null) {
                for (File element : files) {
                    System.out.println(element.getCanonicalPath());
                    recursiveFileReader(element.getCanonicalPath());
                }
            }
        }
    }
}

