package storage;

import model.Resume;

public class MapUuidStorage extends AbstractMapStorage {

    @Override
    protected Object findSearchKey(String uuid) {
        return storage.containsKey(uuid) ? uuid : null;
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.remove(searchKey);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void doUpdate(Object searchKey, Resume resume) {
        String uuid = resume.getUuid();
        storage.put(uuid, resume);
    }
}