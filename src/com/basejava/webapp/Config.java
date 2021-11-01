package com.basejava.webapp;

import com.basejava.webapp.storage.SqlStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    //    private static final File PROPS = new File(getHomeDir(), "config\\resumes.properties");

//    private static final String PROPS = "config\\resumes.properties";
    private static final String PROPS = "/resumes.properties";



    private static final Config INSTANCE = new Config();

    private Properties props = new Properties();
    private File storageDir;
    private SqlStorage sqlStorage;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
//        try (InputStream is = new FileInputStream(PROPS)) {
        try (InputStream is = Config.class.getResourceAsStream(PROPS)) {
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            sqlStorage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS);
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public static SqlStorage getSqlStorage() {
        return INSTANCE.sqlStorage;
    }

//    private static File getHomeDir() {
//        String prop = System.getProperty("homeDir");
//        File homeDir = new File(prop == null ? "." : prop);
//        if (!homeDir.isDirectory()) {
//            throw new IllegalStateException(homeDir + " is not a directory.");
//        }
//        return homeDir;
//    }
}
