package storage;

import model.Resume;
import java.util.Map;

public class MapResumeStorage extends AbstractMapStorage {

    @Override
    protected Object findSearchKey(String uuid) {
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            if (entry.getValue().getUuid().equals(uuid)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.remove(((Resume) searchKey).getUuid());
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get(((Resume) searchKey).getUuid());
    }

    @Override
    protected void doUpdate(Object searchKey, Resume resume) {
        String uuid = resume.getUuid();
        storage.put(uuid, (Resume) searchKey);
    }
}
