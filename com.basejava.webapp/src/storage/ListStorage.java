package storage;

import model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private final List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Object findSearchKey(String uuid) {
        return storage.indexOf(new Resume(uuid));
    }

    @Override
    protected void saveToStorage(Object searchKey, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void deleteFromStorage(Object searchKey) {
        storage.remove((int) searchKey);
    }

    @Override
    protected Resume getFromStorage(Object searchKey) {
        return storage.get((int) searchKey);
    }

    @Override
    protected void updateStorage(Object searchKey, Resume resume) {
        storage.set((int) searchKey, resume);
    }

    @Override
    protected boolean checkIfAbsent(Object searchKey) {
        return (int) searchKey < 0;
    }
}