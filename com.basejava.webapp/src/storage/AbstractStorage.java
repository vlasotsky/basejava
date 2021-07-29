package storage;

import exception.ExistingStorageException;
import exception.NotExistingStorageException;
import model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    public static final Comparator<Resume> STORAGE_COMPARATOR =
            Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    protected abstract Object findSearchKey(String uuid);

    protected abstract void doSave(Object searchIndex, Resume resume);

    protected abstract void doDelete(Object searchKey);

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doUpdate(Object searchKey, Resume resume);

    protected abstract boolean isExist(Object searchKey);

    protected abstract List<Resume> getList();

    public final List<Resume> getAllSorted() {
        List<Resume> list = getList();
        list.sort(STORAGE_COMPARATOR);
        return list;
    }

    @Override
    public final void update(Resume resume) {
        String uuid = resume.getUuid();
        doUpdate(getSearchKeyIfResumeExists(uuid), resume);
        System.out.println("ID " + uuid + " was updated.");
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey = findSearchKey(uuid);
        if (!isExist(searchKey)) {
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

    private Object getSearchKeyIfResumeExists(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new NotExistingStorageException(uuid);
        }
        return searchKey;
    }
}