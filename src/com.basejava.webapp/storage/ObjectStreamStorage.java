package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.*;

public class ObjectStreamStorage extends AbstractFileStorage {
    protected ObjectStreamStorage(File directory) {
        super(directory);
    }

    @Override
    protected void doWrite(OutputStream outputStream, Resume resume) throws IOException {
        try (ObjectOutputStream stream = new ObjectOutputStream(outputStream)) {
            stream.writeObject(resume);
        }
    }

    @Override
    protected Resume doRead(BufferedInputStream inputStream) throws IOException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            return (Resume) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException(null, "Error while reading a Resume", e);
        }
    }
}
