package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.strategy.StreamSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private final StreamSerializer streamSerializer;

    protected FileStorage(File directory, StreamSerializer streamSerializer) {
        Objects.requireNonNull(directory, "Directory must not be null");
        Objects.requireNonNull(streamSerializer, "Strategy cannot be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not a directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writable");
        }
        this.directory = directory;
        this.streamSerializer = streamSerializer;
    }

    @Override
    protected File findSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doSave(File file, Resume resume) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Couldn't create file " + file.getAbsolutePath(), file.getName(), e);
        }
        doUpdate(file, resume);
    }


    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("File deletion error", file.getName());
        }
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return streamSerializer.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Error while reading a file", file.getName(), e);
        }
    }

    @Override
    protected void doUpdate(File file, Resume resume) {
        try {
            streamSerializer.doWrite(new BufferedOutputStream(new FileOutputStream(file)), resume);
        } catch (IOException e) {
            throw new StorageException("Error while writing into a file", resume.getUuid(), e);
        }
    }


    @Override
    protected boolean isNotExisting(File file) {
        return !file.exists();
    }

    @Override
    protected List<Resume> doCopyAll() {
        File[] fileArray = getFileArray();
        List<Resume> list = new ArrayList<>(fileArray.length);
        for (File file : fileArray) {
            list.add(doGet(file));
        }
        return list;
    }

    @Override
    public void clear() {
        for (File element : getFileArray()) {
            doDelete(element);
        }
    }

    @Override
    public int size() {
        return getFileArray().length;
    }

    private File[] getFileArray() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Error while reading a file");
        }
        return files;
    }
}
