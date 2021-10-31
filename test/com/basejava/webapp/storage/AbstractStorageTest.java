package com.basejava.webapp.storage;

import com.basejava.webapp.Config;
import com.basejava.webapp.exception.ExistingStorageException;
import com.basejava.webapp.exception.NotExistingStorageException;
import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.basejava.webapp.TestData.*;


public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = Config.get().getStorageDir();

    protected Storage storage;

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
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
        Resume resumeToTest = RESUME_1;
        resumeToTest.setContact(ContactType.EMAIL_ADDRESS, "gmail.com");
        resumeToTest.getContacts().remove(ContactType.PERSONAL_WEBPAGE);
        storage.update(resumeToTest);
        Assert.assertEquals(resumeToTest, storage.get(UUID_1));
        Assert.assertEquals(3, storage.size());
    }

    @Test(expected = NotExistingStorageException.class)
    public void updateNotExisting() {
        storage.update(RESUME_DUMMY);
    }

    @Test
    public void save() {
        storage.save(RESUME_TEST);
        Resume actual = storage.get(UUID_TEST);
        Assert.assertEquals(RESUME_TEST, actual);
        Assert.assertEquals(4, storage.size());
    }

    @Test(expected = ExistingStorageException.class)
    public void saveAlreadyExisting() {
        storage.save(RESUME_1);
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
        storage.delete(UUID_DUMMY);
    }

    @Test
    public void get() {
        Assert.assertEquals(RESUME_1, storage.get(UUID_1));
    }

    @Test(expected = NotExistingStorageException.class)
    public void getNotExisting() {
        storage.get(UUID_DUMMY);
    }

    @Test
    public void getAllSorted() {
        List<Resume> listToTest = new ArrayList<>() {{
            add(RESUME_1);
            add(RESUME_2);
            add(RESUME_3);
        }};
        listToTest.sort(AbstractStorage.STORAGE_COMPARATOR);
        List<Resume> expected = storage.getAllSorted();
        Assert.assertEquals(listToTest, expected);
    }
}
