package storage;

import model.Resume;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new LinkedHashMap<>();

    @Override
    protected Object findIndex(String uuid) {
        return storage.containsKey(uuid) ? Integer.parseInt(uuid.substring(4)) : -1;
    }

    @Override
    protected void saveToStorage(int foundIndex, Resume resume) {
        String uuid = resume.getUuid();
        storage.put(uuid, resume);
    }

    @Override
    protected void deleteFromStorage(Object searchKey) {
        storage.remove("uuid" + searchKey.toString());
    }

    @Override
    protected Resume getFromStorage(Object searchKey) {
        return storage.get("uuid" + searchKey.toString());
    }

    @Override
    protected void updateStorage(int foundIndex, Resume resume) {
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
}