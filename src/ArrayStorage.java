import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
        Arrays.fill(storage, null);
    }

    void save(Resume r) {
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] == null) {
                storage[i] = r;
                break;
            }
        }
    }

    Resume get(String uuid) {
        if (uuid.equals("dummy")) {
            Resume dummy = new Resume();
            dummy.uuid = "dummy";
            return dummy;
        }
        for (Resume element : storage) {
            if (element.uuid.equals(uuid)) {
                return element;
            }
        }
        return null;
    }

    void delete(String uuid) {

        if (storage[size() - 1].uuid.equals(uuid)) {
            storage[size() - 1] = null;
        } else {
            for (int i = 0; i < size(); i++) {
                if (storage[i].uuid.equals(uuid)) {
                    if (size() - (i + 1) >= 0) System.arraycopy(storage, i + 1, storage, i + 1 - 1, size() - (i + 1));
                    storage[size() - 1] = null;
                }
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] arrayResume = new Resume[size()];
        if (size() >= 0) System.arraycopy(storage, 0, arrayResume, 0, size());
        return arrayResume;
    }

    int size() {
        int size = 0;
        for (Resume resume : storage) {
            if (resume != null) {
                size++;
            }
        }
        return size;
    }
}
