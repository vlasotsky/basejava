package storage;

import exception.ExistingStorageException;
import exception.NotExistingStorageException;
import exception.StorageException;
import model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10_000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public final void update(Resume resume) {
        String uuid = resume.getUuid();
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            throw new NotExistingStorageException(uuid);
        } else {
            storage[foundIndex] = resume;
            System.out.println("ID " + uuid + " was updated.");
        }
    }

    public final void save(Resume resume) {
        String uuid = resume.getUuid();
        int foundIndex = findIndex(uuid);
        if (size == storage.length) {
            throw new StorageException("Storage is full", uuid);
        } else if (foundIndex < 0) {
            saveToArray(foundIndex, resume);
            size++;
        } else {
            throw new ExistingStorageException(uuid);
        }
    }

    protected abstract void saveToArray(int foundIndex, Resume resume);

    public final void delete(String uuid) {
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            throw new NotExistingStorageException(uuid);
        } else if (size - foundIndex >= 0) {
            System.arraycopy(storage, foundIndex + 1, storage, foundIndex, size - (foundIndex + 1));
            size--;
        }
    }

    public int size() {
        return size;
    }

    public final Resume get(String uuid) {
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            throw new NotExistingStorageException(uuid);
        }
        return storage[foundIndex];
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    protected abstract int findIndex(String uuid);
}
