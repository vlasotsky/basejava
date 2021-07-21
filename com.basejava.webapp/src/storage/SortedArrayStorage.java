package storage;

import model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    protected Object findIndex(String uuid) {
        return Arrays.binarySearch(storage, 0, size, new Resume(uuid));
    }

    @Override
    protected void saveToArray(int foundIndex, Resume resume) {
        int neededIndex = Math.abs(foundIndex) - 1;
        System.arraycopy(storage, neededIndex, storage, neededIndex + 1, size - neededIndex);
        storage[neededIndex] = resume;
    }
}

