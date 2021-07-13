package storage;

import exception.NotExistingStorageException;
import exception.StorageException;
import model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractArrayStorageTest {
    //    protected static final int STORAGE_LIMIT = 10_000;
    protected Storage storage;

    private static final String UUID_1 = "uuid_1";
    private static final String UUID_2 = "uuid_2";
    private static final String UUID_3 = "uuid_3";

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }

    @Test(expected = StorageException.class)
    public void storageOverflow() {
//        this.storage = new ArrayStorage();
        try {


            storage.save(new Resume());
            Assert.fail("Storage has been filled completely before it was expected.");
        } catch (StorageException storageException) {

        }

    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() {

    }

    @Test
    public void save() {
        Resume resume = new Resume("uuid_5");
        storage.save(new Resume("uuid_5"));
        Assert.assertEquals(resume, storage.get("uuid_5"));
    }

    @Test(expected = NotExistingStorageException.class)
    public void delete() {
        storage.delete("uuid_1");
        storage.get("uuid_1");
    }

    @Test
    public void get() {
        Resume resume = new Resume(UUID_1);
        Assert.assertEquals(resume, storage.get("uuid1"));
    }


    @Test(expected = NotExistingStorageException.class)
    public void getNotExisting() {
        storage.get("dummy");
    }

    @Test
    public void getAll() {
    }
}