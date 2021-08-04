package storage;

import model.Resume;

public class MapUuidStorage extends AbstractMapStorage<String> {

    @Override
    protected String findSearchKey(String uuid) {
        return storage.containsKey(uuid) ? uuid : null;
    }

    @Override
    protected void doDelete(String uuid) {
        storage.remove(uuid);
    }

    @Override
    protected Resume doGet(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void doUpdate(String searchKey, Resume resume) {
        String uuid = resume.getUuid();
        storage.put(uuid, resume);
    }
}