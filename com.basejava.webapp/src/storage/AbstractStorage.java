package storage;

import exception.NotExistingStorageException;
import model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public final void update(Resume resume) {
        String uuid = resume.getUuid();
        updateStorage(checkIfAbsent(uuid), resume);
        System.out.println("ID " + uuid + " was updated.");
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey = findSearchKey(uuid);
        saveToStorage(searchKey, resume);
    }

    @Override
    public Resume get(String uuid) {
        return getFromStorage(checkIfAbsent(uuid));
    }

    @Override
    public void delete(String uuid) {
        deleteFromStorage(checkIfAbsent(uuid));
    }

    protected abstract Object findSearchKey(String uuid);

    protected abstract void saveToStorage(Object searchIndex, Resume resume);

    protected abstract void deleteFromStorage(Object searchKey);

    protected abstract Resume getFromStorage(Object searchKey);

    protected abstract void updateStorage(Object searchKey, Resume resume);

        protected Object checkIfAbsent(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if ((int) searchKey < 0) {
            throw new NotExistingStorageException(uuid);
        }
        return searchKey;
    }
//    protected abstract Object checkIfAbsent(String uuid);
}