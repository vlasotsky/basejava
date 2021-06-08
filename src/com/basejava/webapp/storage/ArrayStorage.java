package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    public int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume resume) {
        if (isPresent(resume.uuid)) {
            for (int i = 0; i < size; i++) {
                if (storage[i].uuid.equals(resume.uuid)) {
                    storage[i] = resume;
                }
            }
        } else {
            System.out.println("There is no such ID to update");
        }
    }

    public void save(Resume r) {
        if (isPresent(r)) {
            System.out.println("This ID already exists");
        } else {
            if (size == storage.length) {
                System.out.println("Storage is full");
            } else {
                storage[size] = r;
                size++;
            }
        }
    }

    public Resume get(String uuid) {
        if (isPresent(uuid)) {
            for (int i = 0; i < size; i++) {
                if (storage[i].uuid.equals(uuid)) {
                    return storage[i];
                }
            }
        } else {
            System.out.println("There is no such ID to get from the storage");
        }
        return null;
    }

    public void delete(String uuid) {
        if (!isPresent(uuid)) {
            System.out.println("There is no such ID to delete");
        } else {
            if (storage[size - 1].uuid.equals(uuid)) {
                storage[size - 1] = null;
            } else {
                for (int i = 0; i <= size; i++) {
                    if (storage[i].uuid.equals(uuid)) {
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
        if (element instanceof String) {
            for (int i = 0; i < size; i++) {
                if (storage[i].uuid.equals(element)) {
                    return true;
                }
            }
        } else if (element instanceof Resume) {
            return Arrays.asList(storage).contains(element);
        }
        return false;
    }
}
