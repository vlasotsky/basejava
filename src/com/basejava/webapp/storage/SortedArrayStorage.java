package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    protected int findIndex(String uuid) {
        return Arrays.binarySearch(storage, 0, size, new Resume(uuid));
    }

    @Override
    protected void saveToArray(int foundIndex, Resume resume) {
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
    }
}
