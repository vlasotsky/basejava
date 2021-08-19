package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ObjectStreamPathStorage extends AbstractPathStorage {
    protected ObjectStreamPathStorage(String dir) {
        super(dir);
    }

    @Override
    protected void doWrite(OutputStream outputStream, Resume resume) throws IOException {

    }

    @Override
    protected Resume doRead(BufferedInputStream inputStream) throws IOException {
        return null;
    }
}
