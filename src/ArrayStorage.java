import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    public int size;

    void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    void save(Resume r) {
        storage[size] = r;
        size++;
    }

    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        if (storage[size - 1].uuid.equals(uuid)) {
            storage[size - 1] = null;
        } else {
            for (int i = 0; i <= size; i++) {
                if (storage[i].uuid.equals(uuid)) {
                    if (size - (i + 1) >= 0)
                        System.arraycopy(storage, i + 1, storage, i, size - (i + 1));
                    storage[size - 1] = null;
                    break;
                }
            }
        }
        size--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] arrayResume = new Resume[size];
        if (size >= 0) System.arraycopy(storage, 0, arrayResume, 0, size);
        return arrayResume;
    }

    int size() {
        return size;
    }
}
