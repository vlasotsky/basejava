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
        try {
            String idToFind = resume.getUuid();
            storage[indexFound(idToFind)] = resume;
            System.out.println("ID " + resume.getUuid() + " was updated.");
        } catch (Exception e) {
            System.out.println("ID " + resume.getUuid() + " was not found.");
        }
    }

    public void save(Resume r) {
        String idToFind = r.getUuid();
        if (size == storage.length) {
            System.out.println("Storage is full");
        } else if (indexFound(idToFind) == -1) {
            storage[size] = r;
            size++;
        } else {
            System.out.println("ID " + r.getUuid() + " already exists.");
        }
    }

    public Resume get(String uuid) {
        try {
            return storage[indexFound(uuid)];
        } catch (Exception e) {
            System.out.println("ID " + uuid + " was not found.");
            return null;
        }
    }

    public void delete(String uuid) {
        try {
            if (size - indexFound(uuid) >= 0)
                System.arraycopy(storage, indexFound(uuid) + 1, storage, indexFound(uuid), size - indexFound(uuid));
            size--;
        } catch (Exception e) {
            System.out.println("ID " + uuid + " was not found.");
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

    public int indexFound(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
