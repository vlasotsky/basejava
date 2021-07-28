package storage;

import exception.ExistingStorageException;
import exception.NotExistingStorageException;
import model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract Object findSearchKey(String uuid);

    protected abstract void doSave(Object searchIndex, Resume resume);

    protected abstract void doDelete(Object searchKey);

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doUpdate(Object searchKey, Resume resume);

    protected abstract boolean checkIfAbsent(Object searchKey);

    @Override
    public final void update(Resume resume) {
        String uuid = resume.getUuid();
        doUpdate(getSearchKeyIfResumeExists(uuid), resume);
        System.out.println("ID " + uuid + " was updated.");
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey =  findSearchKey(uuid);
        if (!checkIfAbsent(searchKey)) {
            throw new ExistingStorageException(uuid);
        }
        doSave(searchKey, resume);
    }

    @Override
    public Resume get(String uuid) {
        return doGet(getSearchKeyIfResumeExists(uuid));
    }

    @Override
    public void delete(String uuid) {
        doDelete(getSearchKeyIfResumeExists(uuid));
    }

    protected Object getSearchKeyIfResumeExists(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if (checkIfAbsent(searchKey)) {
            throw new NotExistingStorageException(uuid);
        }
        return searchKey;
    }
}