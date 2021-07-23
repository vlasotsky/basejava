package storage;

import exception.NotExistingStorageException;
import model.Resume;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new LinkedHashMap<>();

    @Override
    protected Object findSearchKey(String uuid) {
        return storage.containsKey(uuid) ? uuid : null;
    }

    @Override
    protected void saveToStorage(Object searchIndex, Resume resume) {
        String uuid = resume.getUuid();
        storage.put(uuid, resume);
    }

    @Override
    protected void deleteFromStorage(Object searchKey) {
        storage.remove(searchKey);
    }

    @Override
    protected Resume getFromStorage(Object searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void updateStorage(Object searchKey, Resume resume) {
        String uuid = resume.getUuid();
        storage.put(uuid, resume);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Object checkIfAbsent(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if (searchKey == null) {
            throw new NotExistingStorageException(uuid);
        }
        return searchKey;
    }
}