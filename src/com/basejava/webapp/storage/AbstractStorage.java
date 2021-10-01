package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ExistingStorageException;
import com.basejava.webapp.exception.NotExistingStorageException;
import com.basejava.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    public static final Comparator<Resume> STORAGE_COMPARATOR =
            Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    protected abstract SK findSearchKey(String uuid);

    protected abstract void doSave(SK searchIndex, Resume resume);

    protected abstract void doDelete(SK searchKey);

    protected abstract Resume doGet(SK searchKey);

    protected abstract void doUpdate(SK searchKey, Resume resume);

    protected abstract boolean isNotExisting(SK searchKey);

    protected abstract List<Resume> doCopyAll();

    public final List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> list = doCopyAll();
        list.sort(STORAGE_COMPARATOR);
        return list;
    }

    @Override
    public final void update(Resume resume) {
        LOG.info("Update " + resume.getFullName());
        String uuid = resume.getUuid();
        doUpdate(getSearchKeyIfResumeExists(uuid), resume);
        System.out.println("ID " + uuid + " was updated.");
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume.getFullName());
        String uuid = resume.getUuid();
        SK searchKey = findSearchKey(uuid);
        if (!isNotExisting(searchKey)) {
            throw new ExistingStorageException(uuid);
        }
        doSave(searchKey, resume);
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return doGet(getSearchKeyIfResumeExists(uuid));
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        doDelete(getSearchKeyIfResumeExists(uuid));
    }

    private SK getSearchKeyIfResumeExists(String uuid) {
        SK searchKey = findSearchKey(uuid);
        if (isNotExisting(searchKey)) {
            LOG.warning("ID " + uuid + " was not found.");
            throw new NotExistingStorageException(uuid);
        }
        return searchKey;
    }
}