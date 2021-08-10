package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;
import java.util.*;

public abstract class AbstractMapStorage<SK> extends AbstractStorage<SK> {

    protected final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected abstract SK findSearchKey(String uuid);

    @Override
    protected abstract void doDelete(SK searchKey);

    @Override
    protected abstract Resume doGet(SK searchKey);

    @Override
    protected abstract void doUpdate(SK searchKey, Resume resume);

    @Override
    protected void doSave(SK searchIndex, Resume resume) {
        String uuid = resume.getUuid();
        storage.put(uuid, resume);
    }

    @Override
    protected boolean isNotExisting(SK searchKey) {
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
    protected List<Resume> doCopyAll() {
        return new ArrayList<>(storage.values());
    }
}
