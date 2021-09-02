package com.basejava.webapp.storage;

import com.basejava.webapp.ResumeTestData;
import com.basejava.webapp.exception.ExistingStorageException;
import com.basejava.webapp.exception.NotExistingStorageException;
import com.basejava.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = new File(".\\basejava\\test\\FilesDrop");

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
        storage.save(ResumeTestData.makeTestResume(UUID_1, "Mary"));
        storage.save(ResumeTestData.makeTestResume(UUID_2, "David"));
        storage.save(ResumeTestData.makeTestResume(UUID_3, "Zoe"));
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
        Resume resumeToTest = ResumeTestData.makeTestResume(UUID_1, "Mary");
        storage.update(resumeToTest);
        Assert.assertEquals(resumeToTest, storage.get(UUID_1));
        Assert.assertEquals(3, storage.size());
    }

    @Test(expected = NotExistingStorageException.class)
    public void updateNotExisting() {
        storage.update(ResumeTestData.makeTestResume("uuid8", "dummy"));
    }

    @Test
    public void save() {
        Resume resume = ResumeTestData.makeTestResume("uuid25", "Gabriel");
        storage.save(ResumeTestData.makeTestResume("uuid25", "Gabriel"));
        Assert.assertEquals(resume, storage.get("uuid25"));
        Assert.assertEquals(4, storage.size());
    }

    @Test(expected = ExistingStorageException.class)
    public void saveAlreadyExisting() {
        storage.save(ResumeTestData.makeTestResume(UUID_1, "Mary"));
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
        Resume resume = ResumeTestData.makeTestResume(UUID_1, "Mary");
        Assert.assertEquals(resume, storage.get(UUID_1));
    }

    @Test(expected = NotExistingStorageException.class)
    public void getNotExisting() {
        storage.get("dummy");
    }

    @Test
    public void getAllSorted() {
        List<Resume> listToTest = new ArrayList<>() {{
            add(ResumeTestData.makeTestResume(UUID_1, "Mary"));
            add(ResumeTestData.makeTestResume(UUID_2, "David"));
            add(ResumeTestData.makeTestResume(UUID_3, "Zoe"));
        }};
        listToTest.sort(AbstractStorage.STORAGE_COMPARATOR);
        List<Resume> expected = storage.getAllSorted();
        Assert.assertEquals(listToTest, expected);
    }
}
