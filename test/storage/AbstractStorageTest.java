package storage;

import exception.ExistingStorageException;
import exception.NotExistingStorageException;
import model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractStorageTest {
    protected Storage storage;

    private static final String UUID_1 = "uuid_1";
    private static final String UUID_2 = "uuid_2";
    private static final String UUID_3 = "uuid_3";

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }


    @Before
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() {
        Resume resumeToTest = new Resume(UUID_1);
        storage.update(resumeToTest);
        Assert.assertEquals(resumeToTest, storage.get(UUID_1));
    }

    @Test(expected = NotExistingStorageException.class)
    public void updateNotExisting() {
        storage.update(new Resume("uuid_8"));
    }

    @Test
    public void save() {
        Resume resume = new Resume("uuid_5");
        storage.save(new Resume("uuid_5"));
        Assert.assertEquals(resume, storage.get("uuid_5"));
    }

    @Test(expected = ExistingStorageException.class)
    public void saveAlreadyExisting() {
        storage.save(new Resume(UUID_1));
    }

    @Test(expected = NotExistingStorageException.class)
    public void delete() {
        int expectedSize = storage.size() - 1;
        storage.delete(UUID_1);
        storage.get(UUID_1);
        Assert.assertEquals(expectedSize, storage.size());
    }

    @Test(expected = NotExistingStorageException.class)
    public void deleteNotExisting() {
        storage.delete("uuid_8");
    }

    @Test
    public void get() {
        Resume resume = new Resume(UUID_1);
        Assert.assertEquals(resume, storage.get(UUID_1));
    }

    @Test(expected = NotExistingStorageException.class)
    public void getNotExisting() {
        storage.get("dummy");
    }

    @Test
    @SuppressWarnings("deprecation")
    public void getAll() {
        Resume[] arrToTest = {new Resume(UUID_1), new Resume(UUID_2), new Resume(UUID_3)};
        Assert.assertEquals(arrToTest, storage.getAll());
    }
}
