package storage;

import exception.ExistingStorageException;
import exception.NotExistingStorageException;
import model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public final void update(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey = findSearchKey(uuid);
        if (checkIfAbsent(searchKey)) {
            throw new NotExistingStorageException(uuid);
        }
        updateStorage(searchKey, resume);
        System.out.println("ID " + uuid + " was updated.");
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey = findSearchKey(uuid);
        if (!checkIfAbsent(searchKey)) {
            throw new ExistingStorageException(uuid);
        }
        saveToStorage(searchKey, resume);
    }

    @Override
    public Resume get(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if (checkIfAbsent(searchKey)) {
            throw new NotExistingStorageException(uuid);
        }
        return getFromStorage(searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if (checkIfAbsent(searchKey)) {
            throw new NotExistingStorageException(uuid);
        }
        deleteFromStorage(searchKey);
    }

    protected abstract Object findSearchKey(String uuid);

    protected abstract void saveToStorage(Object searchIndex, Resume resume);

    protected abstract void deleteFromStorage(Object searchKey);

    protected abstract Resume getFromStorage(Object searchKey);

    protected abstract void updateStorage(Object searchKey, Resume resume);

    protected abstract boolean checkIfAbsent(Object searchKey);
}