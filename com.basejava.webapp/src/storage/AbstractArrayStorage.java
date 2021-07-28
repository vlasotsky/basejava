package storage;

import exception.StorageException;
import model.Resume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    protected abstract void saveToArray(int foundIndex, Resume resume);

    protected abstract Object findSearchKey(String uuid);

    @Override
    protected Resume doGet(Object searchKey) {
        return storage[(int) searchKey];
    }

    @Override
    protected boolean checkIfAbsent(Object searchKey) {
        return (int) searchKey < 0;
    }

    @Override
    protected void doUpdate(Object searchKey, Resume resume) {
        storage[(int) searchKey] = resume;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public int size() {
        return size;
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = new ArrayList<>(Arrays.asList(storage));
        list.removeAll(Collections.singletonList(null));
        list.sort((o1, o2) -> {
            if (o1.getFullName().equals(o2.getFullName())) {
                return o1.getUuid().compareTo(o2.getUuid());
            }
            return o1.getFullName().compareTo(o2.getFullName());
        });
        return list;
    }

    @Override
    protected void doDelete(Object searchKey) {
        int keyInUse = (int) searchKey;
        if (size - (keyInUse + 1) >= 0) {
            System.arraycopy(storage, keyInUse + 1, storage, keyInUse, size - (keyInUse + 1));
        }
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected void doSave(Object searchKey, Resume resume) {
        String uuid = resume.getUuid();
        if (size == storage.length) {
            throw new StorageException("Storage is full", uuid);
        }
        saveToArray((int) searchKey, resume);
        size++;
    }
}