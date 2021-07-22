package storage;

import exception.ExistingStorageException;
import exception.NotExistingStorageException;
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

    protected abstract Object findSearchKey(String uuid);

    @Override
    protected void deleteFromStorage(Object searchKey) {
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
    protected void saveToStorage(Resume resume) {
        String uuid = resume.getUuid();
        Integer searchKey = (Integer) findSearchKey(uuid);
        if (size == storage.length) {
            throw new StorageException("Storage is full", uuid);
        } else if (searchKey >= 0) {
            throw new ExistingStorageException(uuid);
        }
        saveToArray(searchKey, resume);
        size++;
    }

    @Override
    protected void updateStorage(Object searchKey, Resume resume) {
        storage[(int) searchKey] = resume;
    }

    protected abstract void saveToArray(int foundIndex, Resume resume);

    @Override
    protected Object checkIfAbsent(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if ((int) searchKey < 0) {
            throw new NotExistingStorageException(uuid);
        }
        return searchKey;
    }
}
