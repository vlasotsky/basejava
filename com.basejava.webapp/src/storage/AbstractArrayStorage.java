package storage;

import exception.StorageException;
import model.Resume;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected static final int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    protected abstract void saveToArray(int foundIndex, Resume resume);

    protected abstract Integer findSearchKey(String uuid);

    @Override
    protected Resume doGet(Integer searchKey) {
        return storage[searchKey];
    }

    @Override
    protected boolean isNotExisting(Integer searchKey) {
        return searchKey < 0;
    }

    @Override
    protected void doUpdate(Integer searchKey, Resume resume) {
        storage[searchKey] = resume;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public int size() {
        return size;
    }

    @Override
    protected List<Resume> doCopyAll() {
        return new ArrayList<>(Arrays.asList(Arrays.copyOf(storage, size)));
    }

    @Override
    protected void doDelete(Integer searchKey) {
        int keyInUse = searchKey;
        if (size - (keyInUse + 1) >= 0) {
            System.arraycopy(storage, keyInUse + 1, storage, keyInUse, size - (keyInUse + 1));
        }
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected void doSave(Integer searchKey, Resume resume) {
        String uuid = resume.getUuid();
        if (size == storage.length) {
            throw new StorageException("Storage is full", uuid);
        }
        saveToArray(searchKey, resume);
        size++;
    }
}