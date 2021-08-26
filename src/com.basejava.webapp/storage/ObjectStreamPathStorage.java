package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.*;

public class ObjectStreamPathStorage extends AbstractPathStorage implements ObjectStreamStrategy {
    protected ObjectStreamPathStorage(String dir) {
        super(dir);
    }

    @Override
    public void doWrite(OutputStream outputStream, Resume resume) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(outputStream)) {
            oos.writeObject(resume);
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(inputStream)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error while reading a Resume", null, e);
        }
    }


}
