package com.basejava.webapp.storage;

public class ObjectStreamPathStorageTest extends ObjectStreamStrategyTest {

    public ObjectStreamPathStorageTest() {
        super(new ObjectStreamPathStorage(STORAGE_DIR.getPath()));
    }
}
