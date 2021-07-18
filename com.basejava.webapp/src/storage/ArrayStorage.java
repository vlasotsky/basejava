package storage;

import model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected void saveToArray(int foundIndex, Resume resume) {
        if (foundIndex < 0) {
            storage[size] = resume;
            size++;
        } else {
            storage[foundIndex] = resume;
        }

    }

    protected int findIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
