package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 3;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public final void update(Resume resume) {
        String uuid = resume.getUuid();
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            System.out.println("ID " + uuid + " was not found.");
        } else {
            storage[foundIndex] = resume;
            System.out.println("ID " + uuid + " was updated.");
        }
    }

    public final void save(Resume resume) {
        String uuid = resume.getUuid();
        int foundIndex = findIndex(uuid);
        if (size == storage.length) {
            System.out.println("Storage is full");
        } else if (foundIndex < 0) {
            saveToArray(foundIndex, resume);
            size++;
        } else {
            System.out.println("ID " + uuid + " already exists.");
        }
    }

    protected abstract void saveToArray(int foundIndex, Resume resume);

    public final void delete(String uuid) {
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            System.out.println("ID " + uuid + " was not found.");
        } else if (size - foundIndex >= 0) {
            System.arraycopy(storage, foundIndex + 1, storage, foundIndex, size - (foundIndex + 1));
            size--;
        }
    }

    public int size() {
        return size;
    }

    public final Resume get(String uuid) {
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            System.out.println("ID " + uuid + " was not found.");
            return null;
        }
        return storage[foundIndex];
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    protected abstract int findIndex(String uuid);
}
