package storage;

import exception.ExistingStorageException;
import exception.NotExistingStorageException;
import model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public final void update(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey = findIndex(uuid);
        if ((int) searchKey < 0) {
            throw new NotExistingStorageException(uuid);
        }
        updateStorage((int) searchKey, resume);
        System.out.println("ID " + uuid + " was updated.");
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey = findIndex(uuid);
        if ((Integer) searchKey >= 0) {
            throw new ExistingStorageException(uuid);
        }
        saveToStorage((int) searchKey, resume);
    }

    @Override
    public Resume get(String uuid) {
        Object searchKey = findIndex(uuid);
        if ((int) searchKey < 0) {
            throw new NotExistingStorageException(uuid);
        }
        return getFromStorage(searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = findIndex(uuid);
        if ((int) searchKey < 0) {
            throw new NotExistingStorageException(uuid);
        }
        deleteFromStorage(searchKey);
    }

    protected abstract Object findIndex(String uuid);

    protected abstract void saveToStorage(int foundIndex, Resume resume);

    protected abstract void deleteFromStorage(Object searchKey);

    protected abstract Resume getFromStorage(Object searchKey);

    protected abstract void updateStorage(int foundIndex, Resume resume);
}