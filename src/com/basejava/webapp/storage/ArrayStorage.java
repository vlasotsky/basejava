package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10_000];
    private int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume resume) {
        String uuid = resume.getUuid();
        int foundIndex = findIndex(uuid);
        if (foundIndex == -1) {
            System.out.println("ID " + resume.getUuid() + " was not found.");
        } else {
            storage[findIndex(uuid)] = resume;
            System.out.println("ID " + resume.getUuid() + " was updated.");
        }
    }

    public void save(Resume r) {
        String uuid = r.getUuid();
        if (size == storage.length) {
            System.out.println("Storage is full");
        } else if (findIndex(uuid) == -1) {
            storage[size] = r;
            size++;
        } else {
            System.out.println("ID " + r.getUuid() + " already exists.");
        }
    }

    public Resume get(String uuid) {
        int foundIndex = findIndex(uuid);
        if (foundIndex == -1) {
            System.out.println("ID " + uuid + " was not found.");
        } else {
            return storage[foundIndex];
        }
        return null;
    }

    public void delete(String uuid) {
        int foundIndex = findIndex(uuid);
        if (foundIndex == -1) {
            System.out.println("ID " + uuid + " was not found.");
        } else {
            if (size - foundIndex >= 0)
                System.arraycopy(storage, foundIndex + 1, storage, foundIndex, size - foundIndex);
            size--;
        }
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
