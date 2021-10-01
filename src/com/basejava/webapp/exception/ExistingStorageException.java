package com.basejava.webapp.exception;

public class ExistingStorageException extends StorageException {
    public ExistingStorageException(String uuid) {
        super("ID " + uuid + " already exists.", uuid);
    }
}
