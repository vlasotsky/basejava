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
        if (isPresent(resume.getUuid())) {
            for (int i = 0; i < size; i++) {
                if (storage[i].getUuid().equals(resume.getUuid())) {
                    storage[i] = resume;
                    System.out.println("ID " + resume.getUuid() + " was updated.");
                }
            }
        }
    }

    public void save(Resume r) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(r.getUuid())) {
                System.out.println("ID " + r.getUuid() + " already exists.");
                return;
            }
        }
        if (size == storage.length) {
            System.out.println("Storage is full");
        } else {
            storage[size] = r;
            size++;
        }
    }

    public Resume get(String uuid) {
        if (isPresent(uuid)) {
            for (int i = 0; i < size; i++) {
                if (storage[i].getUuid().equals(uuid)) {
                    return storage[i];
                }
            }
        }
        return null;
    }

    public void delete(String uuid) {
        if (isPresent(uuid)) {
            if (storage[size - 1].getUuid().equals(uuid)) {
                storage[size - 1] = null;
            } else {
                for (int i = 0; i <= size; i++) {
                    if (storage[i].getUuid().equals(uuid)) {
                        if (size - (i + 1) >= 0)
                            System.arraycopy(storage, i + 1, storage, i, size - (i + 1));
                        storage[size - 1] = null;
                        break;
                    }
                }
            }
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

    public <T> boolean isPresent(T element) {
        for (int i = 0; i < size; i++) {
            if (element instanceof String) {
                if (storage[i].getUuid().equals(element)) {
                    return true;
                }
            } else if (element instanceof Resume) {
                if (storage[i].getUuid().equals(((Resume) element).getUuid())) {
                    return true;
                }
            }
        }
        System.out.println("ID " + element + " does not exist.");
        return false;
    }
}
