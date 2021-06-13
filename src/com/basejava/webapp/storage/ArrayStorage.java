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
        if (isPresent(resume.getUuid(), false)) {
            for (int i = 0; i < size; i++) {
                if (storage[i].getUuid().equals(resume.getUuid())) {
                    storage[i] = resume;
                    System.out.println("ID " + resume.getUuid() + " was updated.");
                }
            }
        }
    }

    public void save(Resume r) {
        if (isPresent(r.getUuid(), true)) {
            return;
        }
        if (size == storage.length) {
            System.out.println("Storage is full");
        } else {
            storage[size] = r;
            size++;
        }
    }

    public Resume get(String uuid) {
        if (isPresent(uuid, false)) {
            for (int i = 0; i < size; i++) {
                if (storage[i].getUuid().equals(uuid)) {
                    return storage[i];
                }
            }
        }
        return null;
    }

    public void delete(String uuid) {
        if (isPresent(uuid, false)) {
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

    public <T> boolean isPresent(T element, boolean ifExists) {
        if (element instanceof String) {
            for (int i = 0; i < size; i++) {
                if (storage[i].getUuid().equals(element)) {
                    if (ifExists) {
                        System.out.println("ID " + element + " already exists");
                    }
                    return true;
                }
            }
            if (!ifExists) {
                System.out.println("ID " + element + " does not exist.");
            }
        } else if (element instanceof Resume) {
            if (Arrays.asList(storage).contains(element)) {
                if (ifExists) {
                    System.out.println("ID " + element + " already exists");
                }
                return true;
            } else {
                if (!ifExists) {
                    System.out.println(((Resume) element).getUuid() + " does not exist.");
                }
                return false;
            }
        }
        return false;
    }
}
