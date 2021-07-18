package storage;

import exception.ExistingStorageException;
import exception.NotExistingStorageException;
import model.Resume;

public abstract class AbstractStorage implements Storage {
    @Override
    public abstract void clear();

    @Override
    public final void update(Resume resume) {
        String uuid = resume.getUuid();
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            throw new NotExistingStorageException(uuid);
        } else {
            saveToStorage(foundIndex, resume);
            System.out.println("ID " + uuid + " was updated.");
        }
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            saveToStorage(foundIndex, resume);
        } else {
            throw new ExistingStorageException(uuid);
        }
    }

    @Override
    public Resume get(String uuid) {
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            throw new NotExistingStorageException(uuid);
        }
        return getFromStorage(foundIndex);
    }

    @Override
    public void delete(String uuid) {
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            throw new NotExistingStorageException(uuid);
        } else {
            deleteFromStorage(foundIndex);
        }
    }

    @Override
    public abstract Resume[] getAll();

    @Override
    public abstract int size();

    protected abstract int findIndex(String uuid);

    protected abstract void saveToStorage(int foundIndex, Resume resume);

    protected abstract void deleteFromStorage(int foundIndex);

    protected abstract Resume getFromStorage(int index);
}
