package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.*;

public class ObjectStreamStorage extends AbstractFileStorage implements ObjectStreamStrategy {
    protected ObjectStreamStorage(File directory) {
        super(directory);
    }

    @Override
    public void doWrite(OutputStream outputStream, Resume resume) throws IOException {
        try (ObjectOutputStream stream = new ObjectOutputStream(outputStream)) {
            stream.writeObject(resume);
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            return (Resume) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error while reading a Resume", null, e);
        }
    }
}
