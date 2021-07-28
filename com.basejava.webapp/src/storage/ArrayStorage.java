package storage;

import model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected void saveToArray(int foundIndex, Resume resume) {
        storage[size] = resume;
    }

    protected Object findSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}