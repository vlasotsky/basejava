package storage;

import model.Resume;
import java.util.*;

public abstract class MapStorage extends AbstractStorage {

    protected final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected abstract Object findSearchKey(String uuid);

    @Override
    protected abstract void doDelete(Object searchKey);

    @Override
    protected abstract Resume doGet(Object searchKey);

    @Override
    protected abstract void doUpdate(Object searchKey, Resume resume);

    @Override
    protected void doSave(Object searchIndex, Resume resume) {
        String uuid = resume.getUuid();
        storage.put(uuid, resume);
    }

    @Override
    protected boolean checkIfAbsent(Object searchKey) {
        return searchKey == null;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = new ArrayList<>(storage.values());
        list.sort(Comparator.comparing(Resume::getFullName));
        return list;
    }
}
