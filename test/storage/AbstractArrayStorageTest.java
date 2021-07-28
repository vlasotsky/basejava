package storage;

import exception.StorageException;
import model.Resume;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void storageOverflow() {
        try {
            storage.clear();
            for (int i = 0; i < 10_000; i++) {
                storage.save(new Resume(UUID.randomUUID().toString()));
            }
        } catch (StorageException exception) {
            Assert.fail("Storage had been filled completely before it was expected.");
        }
        storage.save(new Resume(UUID.randomUUID().toString()));
    }
}
