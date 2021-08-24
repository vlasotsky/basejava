package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    private final Path directory;

    protected AbstractPathStorage(String dir) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not a directory or it is not writable");
        }
    }

    protected abstract void doWrite(OutputStream outputStream, Resume resume) throws IOException;

    protected abstract Resume doRead(InputStream inputStream) throws IOException;

    @Override
    protected Path findSearchKey(String uuid) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
            for (Path element : directoryStream) {
                if (element.getFileName().toString().equals(uuid)) {
                    return element;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void doSave(Path path, Resume resume) {
        if (path == null) {
            doUpdate(directory, resume);
        } else {
            doUpdate(path, resume);
        }
    }


    @Override
    protected void doDelete(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new StorageException("Path deletion error", path.getFileName().toString());
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {

//            return doRead(new BufferedInputStream(new FileInputStream(path.toFile())));
            return doRead(new BufferedInputStream(new FileInputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("Error while reading the path", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doUpdate(Path path, Resume resume) {
        try {
            doWrite(new BufferedOutputStream(new FileOutputStream(path + "\\" + resume.getUuid())), resume);
        } catch (IOException e) {
            throw new StorageException("Error while writing the path", resume.getUuid(), e);
        }
    }


    @Override
    protected boolean isNotExisting(Path path) {
        return path == null;
    }

    @Override
    protected List<Resume> doCopyAll() {
        if (directory == null) {
            throw new StorageException("Error while reading the directory path", null);
        }
        List<Resume> list = new ArrayList<>();
        for (Path element : directory) {
            list.add(doGet(element));
        }
        return list;
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Error while deleting the path", null);
        }
    }

    @Override
    public int size() {
        int size = 0;
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
            for (Path element : directoryStream) {
                size++;
            }
        } catch (IOException e) {
            throw new StorageException("Error in attempt to read the directory", null);
        }
        return size;
    }
}

