package com.basejava.webapp.model;

/**
 * Initial resume class
 */
public class Resume {

    // Unique identifier
    private final String uuid;

    public Resume(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return uuid;
    }
}
