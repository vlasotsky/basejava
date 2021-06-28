package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void update(Resume resume) {
        String uuid = resume.getUuid();
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            System.out.println("ID " + uuid + " was not found.");
        } else {
            storage[foundIndex] = resume;
            System.out.println("ID " + uuid + " was updated.");
        }
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        int foundIndex = findIndex(uuid);
        if (size == storage.length) {
            System.out.println("Storage is full");
        } else if (foundIndex < 0) {
            storage[size] = resume;
            size++;
        } else {
            System.out.println("ID " + uuid + " already exists.");
        }
    }

    @Override
    public void delete(String uuid) {
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            System.out.println("ID " + uuid + " was not found.");
        } else {
            for (int i = foundIndex; i < size; i++) {
                storage[i] = storage[i + 1];
            }
            size--;
        }
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    protected int findIndex(String uuid) {
        return Arrays.binarySearch(storage, 0, size, new Resume(uuid));
    }
}
