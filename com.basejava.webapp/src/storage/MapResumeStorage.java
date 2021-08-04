package storage;

import model.Resume;

public class MapResumeStorage extends AbstractMapStorage<Resume> {

    @Override
    protected Resume findSearchKey(String uuid) {
        return storage.get(uuid);
//        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
//            if (entry.getValue().getUuid().equals(uuid)) {
//                return entry.getValue();
//            }
//        }
//        return null;
    }

    @Override
    protected void doDelete(Resume searchKey) {
        storage.remove((searchKey).getUuid());
    }

    @Override
    protected Resume doGet(Resume searchKey) {
        return searchKey;
    }

    @Override
    protected void doUpdate(Resume searchKey, Resume resume) {
        String uuid = resume.getUuid();
        storage.put(uuid, searchKey);
    }
}
