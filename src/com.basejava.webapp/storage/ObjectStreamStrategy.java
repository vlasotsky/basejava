package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ObjectStreamStrategy {

    void doWrite(OutputStream outputStream, Resume resume) throws IOException;

    Resume doRead(InputStream inputStream) throws IOException;
}
