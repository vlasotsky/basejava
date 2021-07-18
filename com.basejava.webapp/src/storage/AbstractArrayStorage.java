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

    protected abstract int findIndex(String uuid);

    @Override
    protected void deleteFromStorage(int foundIndex) {
        if (size - foundIndex >= 0) {
            System.arraycopy(storage, foundIndex + 1, storage, foundIndex, size - (foundIndex + 1));
            size--;
        }
    }

    @Override
    protected Resume getFromStorage(int index) {
        return storage[index];
    }

    @Override
    protected void saveToStorage(int foundIndex, Resume resume) {
        String uuid = resume.getUuid();
        if (size == storage.length && foundIndex < 0) {
            throw new StorageException("Storage is full", uuid);
        } else {
            saveToArray(foundIndex, resume);
        }
    }

    protected abstract void saveToArray(int foundIndex, Resume resume);
}
