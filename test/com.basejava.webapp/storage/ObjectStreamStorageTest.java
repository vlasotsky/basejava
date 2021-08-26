package com.basejava.webapp.storage;

public class ObjectStreamStorageTest extends ObjectStreamStrategyTest{

    public ObjectStreamStorageTest() {
        super(new ObjectStreamStorage(STORAGE_DIR));
    }
}
