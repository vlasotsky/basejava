package storage;

import exception.StorageException;
import model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public int size() {
        return size;
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    protected abstract Object findIndex(String uuid);

    @Override
    protected void deleteFromStorage(Object searchKey) {
//        searchKey = (int) searchKey;
        if (size - (int) searchKey >= 0) {
            System.arraycopy(storage, (int) searchKey + 1, storage, (int) searchKey, size - ((int) searchKey + 1));
            size--;
        }
    }

    @Override
    protected Resume getFromStorage(Object searchKey) {
        return storage[(int) searchKey];
    }

    @Override
    protected void saveToStorage(int foundIndex, Resume resume) {
        String uuid = resume.getUuid();
        if (size == storage.length) {
            throw new StorageException("Storage is full", uuid);
        }
        saveToArray(foundIndex, resume);
        size++;
    }

    @Override
    protected void updateStorage(int foundIndex, Resume resume) {
        storage[foundIndex] = resume;
    }

    protected abstract void saveToArray(int foundIndex, Resume resume);
}
