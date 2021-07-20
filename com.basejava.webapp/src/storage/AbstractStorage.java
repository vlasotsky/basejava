package storage;

import exception.ExistingStorageException;
import exception.NotExistingStorageException;
import model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public final void update(Resume resume) {
        String uuid = resume.getUuid();
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            throw new NotExistingStorageException(uuid);
        }
        updateStorage(foundIndex, resume);
        System.out.println("ID " + uuid + " was updated.");
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        int foundIndex = findIndex(uuid);
        if (foundIndex >= 0) {
            throw new ExistingStorageException(uuid);
        }
        saveToStorage(foundIndex, resume);
    }

    @Override
    public Resume get(String uuid) {
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            throw new NotExistingStorageException(uuid);
        }
        return getFromStorage(foundIndex, uuid);
    }

    @Override
    public void delete(String uuid) {
        int foundIndex = findIndex(uuid);
        if (foundIndex < 0) {
            throw new NotExistingStorageException(uuid);
        }
        deleteFromStorage(foundIndex, uuid);
    }

    protected abstract int findIndex(String uuid);

    protected abstract void saveToStorage(int foundIndex, Resume resume);

    protected abstract void deleteFromStorage(int foundIndex, String uuid);

    protected abstract Resume getFromStorage(int foundIndex, String uuid);

    protected abstract void updateStorage(int foundIndex, Resume resume);
}
