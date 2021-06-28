package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10_000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    public int size() {
        return size;
    }

    public Resume get(String uuid) {
        int foundIndex = findIndex(uuid);
        if (foundIndex == -1) {
            System.out.println("ID " + uuid + " was not found.");
            return null;
        }
        return storage[foundIndex];
    }

    protected abstract int findIndex(String uuid);

}
