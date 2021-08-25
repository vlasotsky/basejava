package com.basejava.webapp.storage;

import com.basejava.webapp.ResumeTestData;
import com.basejava.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

public class StorageStrategySaverTest {
    private final StorageStrategySaver saver = new StorageStrategySaver();
    Resume resumeToTest = ResumeTestData.makeTestResume("uuid40", "Greg");

    @Test
    public void saveWithFile() {
        saver.setStrategy(new ObjectStreamStorage(AbstractStorageTest.STORAGE_DIR));
        saver.strategy.doAction(resumeToTest);

        AbstractFileStorage fileStorage = (AbstractFileStorage) saver.strategy.getObject();

        Assert.assertEquals(resumeToTest, fileStorage.get(resumeToTest.getUuid()));
    }

    @Test
    public void saveWithPath() {
        saver.setStrategy(new ObjectStreamPathStorage(AbstractStorageTest.STORAGE_DIR.getAbsolutePath()));
        saver.strategy.doAction(resumeToTest);

        AbstractPathStorage pathStorage = (AbstractPathStorage) saver.strategy.getObject();

        Assert.assertEquals(resumeToTest, pathStorage.get(resumeToTest.getUuid()));

    }
}
