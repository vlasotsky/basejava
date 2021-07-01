package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        int foundIndex = findIndex(uuid);
        if (size == storage.length) {
            System.out.println("Storage is full");
        } else if (foundIndex < 0) {
            int neededIndex = Math.abs(foundIndex) - 1;
            if (storage[neededIndex] == null) {
                storage[neededIndex] = resume;
                size++;
            } else {
                for (int i = size - 1; i >= neededIndex; i--) {
                    storage[i + 1] = storage[i];
                }
                storage[neededIndex] = resume;
                size++;
            }
        } else {
            System.out.println("ID " + uuid + " already exists.");
        }
    }

    protected int findIndex(String uuid) {
        return Arrays.binarySearch(storage, 0, size, new Resume(uuid));
    }
}
