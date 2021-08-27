package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> implements Strategy {
    private final File directory;
    private Strategy strategy;

    protected FileStorage(File directory, Strategy strategy) {
        Objects.requireNonNull(directory, "Directory must not be null");
        Objects.requireNonNull(strategy, "Strategy cannot be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not a directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writable");
        }
        this.directory = directory;
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
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
            return doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Error while reading a file", file.getName(), e);
        }
    }

    @Override
    protected void doUpdate(File file, Resume resume) {
        try {
            doWrite(new BufferedOutputStream(new FileOutputStream(file)), resume);
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
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Error while reading a file", null);
        }
        List<Resume> list = new ArrayList<>(files.length);
        for (File file : files) {
            list.add(doGet(file));
        }
        return list;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File element : files) {
                doDelete(element);
            }
            System.out.println("Files were deleted");
        }
    }

    @Override
    public int size() {
        String[] list = directory.list();
        if (list == null) {
            throw new StorageException("Error in attempt to read the directory", null);
        }
        return list.length;
    }

    @Override
    public void doWrite(OutputStream outputStream, Resume resume) throws IOException {
        this.strategy.doWrite(outputStream, resume);
    }

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        return this.strategy.doRead(inputStream);
    }
}