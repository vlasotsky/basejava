package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ExistingStorageException;
import com.basejava.webapp.exception.NotExistingStorageException;
import com.basejava.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStorageTest {
    protected Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }


    @Before
    public void setUp() {
        storage.clear();

//        storage.save(ResumeTestData.makeTestResume());
        storage.save(new Resume(UUID_1, "Mary"));
        storage.save(new Resume(UUID_2, "David"));
        storage.save(new Resume(UUID_3, "Zoe"));
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
        Resume resumeToTest = new Resume(UUID_1, "Mary");
        storage.update(resumeToTest);
        Assert.assertEquals(resumeToTest, storage.get(UUID_1));
        Assert.assertEquals(3, storage.size());
    }

    @Test(expected = NotExistingStorageException.class)
    public void updateNotExisting() {
        storage.update(new Resume("uuid8", "dummy"));
    }

    @Test
    public void save() {
        Resume resume = new Resume("uuid25", "Gabriel");
        storage.save(new Resume("uuid25", "Gabriel"));
        Assert.assertEquals(resume, storage.get("uuid25"));
        Assert.assertEquals(4, storage.size());
    }

    @Test(expected = ExistingStorageException.class)
    public void saveAlreadyExisting() {
        storage.save(new Resume(UUID_1, "Mary"));
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
        storage.delete("uuid8");
    }

    @Test
    public void get() {
        Resume resume = new Resume(UUID_1, "Mary");
        Assert.assertEquals(resume, storage.get(UUID_1));
    }

    @Test(expected = NotExistingStorageException.class)
    public void getNotExisting() {
        storage.get("dummy");
    }

    @Test
    public void getAllSorted() {
        List<Resume> listToTest = new ArrayList<>() {{
            add(new Resume(UUID_1, "Mary"));
            add(new Resume(UUID_2, "David"));
            add(new Resume(UUID_3, "Zoe"));
        }};
        listToTest.sort(AbstractStorage.STORAGE_COMPARATOR);
        Assert.assertEquals(listToTest, storage.getAllSorted());
    }
}