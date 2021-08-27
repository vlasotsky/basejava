package com.basejava.webapp.storage;

public class ObjectStreamStorageTest extends AbstractStorageTest{

    public ObjectStreamStorageTest() {
        super(new FileStorage(STORAGE_DIR,new ObjectStreamStorage()));
    }
}
