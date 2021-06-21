package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage implements Storage {
    private static final int STORAGE_LIMIT = 10_000;

    private final Resume[] storage = new Resume[STORAGE_LIMIT];
    private int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume resume) {
        String uuid = resume.getUuid();
        int foundIndex = findIndex(uuid);
        if (foundIndex == -1) {
            System.out.println("ID " + uuid + " was not found.");
        } else {
            storage[foundIndex] = resume;
            System.out.println("ID " + uuid + " was updated.");
        }
    }

    public void save(Resume resume) {
        String uuid = resume.getUuid();
        int foundIndex = findIndex(uuid);
        if (size == storage.length) {
            System.out.println("Storage is full");
        } else if (foundIndex == -1) {
            storage[size] = resume;
            size++;
        } else {
            System.out.println("ID " + uuid + " already exists.");
        }
    }

    public Resume get(String uuid) {
        int foundIndex = findIndex(uuid);
        if (foundIndex == -1) {
            System.out.println("ID " + uuid + " was not found.");
            return null;
        }
        return storage[foundIndex];
    }

    public void delete(String uuid) {
        int foundIndex = findIndex(uuid);
        if (foundIndex == -1) {
            System.out.println("ID " + uuid + " was not found.");
        } else if (size - foundIndex >= 0) {
            System.arraycopy(storage, foundIndex + 1, storage, foundIndex, size - foundIndex);
        }
        size--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int findIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
