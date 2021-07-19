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
    protected int findIndex(String uuid) {
        return storage.indexOf(new Resume(uuid));
    }

    @Override
    protected void saveToStorage(int foundIndex, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void deleteFromStorage(int foundIndex, String uuid) {
        storage.remove(foundIndex);
    }

    @Override
    protected Resume getFromStorage(int index, String uuid) {
        return storage.get(index);
    }

    @Override
    protected void updateStorage(int foundIndex, Resume resume) {
        storage.set(foundIndex, resume);
    }
}
