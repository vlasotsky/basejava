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

public class PathStorage extends AbstractStorage<Path> implements Strategy {
    private final Path directory;
    private Strategy strategy;

    protected PathStorage(String dir, Strategy strategy) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not a directory or it is not writable");
        }
        Objects.requireNonNull(strategy, "Strategy cannot be null");
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

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
            return doRead(new BufferedInputStream(new FileInputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("Error while reading the path", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doUpdate(Path path, Resume resume) {
        try {
            if (path.getFileName().toString().equals(resume.getUuid())) {
                doWrite(new BufferedOutputStream(new FileOutputStream(path.toAbsolutePath().toString())), resume);
            } else {
                doWrite(new BufferedOutputStream(new FileOutputStream(path + "\\" + resume.getUuid())), resume);
            }
        } catch (IOException e) {
            throw new StorageException("Error while writing into the path", resume.getUuid(), e);
        }
    }

    @Override
    protected boolean isNotExisting(Path path) {
        return path == null;
    }

    @Override
    protected List<Resume> doCopyAll() {
        if (directory == null) {
            throw new StorageException("Directory is null", null);
        }
        List<Resume> list = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
            for (Path element : directoryStream) {
                list.add(doGet(element));
            }
        } catch (IOException e) {
            throw new StorageException("Error while reading the directory", null);
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

    @Override
    public void doWrite(OutputStream outputStream, Resume resume) throws IOException {
        this.strategy.doWrite(outputStream, resume);
    }

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        return this.strategy.doRead(inputStream);
    }
}
