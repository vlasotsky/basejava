package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;
import strategy.Strategy;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final Strategy strategy;

    protected PathStorage(String dir, Strategy strategy) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not a directory or it is not writable");
        }
        Objects.requireNonNull(strategy, "Strategy cannot be null");
        this.strategy = strategy;
    }

    @Override
    protected Path findSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected void doSave(Path path, Resume resume) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Error while creating the file", resume.getUuid(), e);
        }
        doUpdate(path, resume);
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path deletion error", path.getFileName().toString());
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return strategy.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Error while reading the path", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doUpdate(Path path, Resume resume) {
        try {
            strategy.doWrite(Files.newOutputStream(path), resume);
        } catch (IOException e) {
            throw new StorageException("Error while writing into the path", resume.getUuid(), e);
        }
    }

    @Override
    protected boolean isNotExisting(Path path) {
        return !Files.exists(path);
    }

    @Override
    protected List<Resume> doCopyAll() {
        return getStreamOfPaths().collect(Collectors.toList()).stream().map(this::doGet).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        getStreamOfPaths().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return (int) getStreamOfPaths().count();
    }

    private Stream<Path> getStreamOfPaths() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Error while reading the directory", null);
        }
    }
}

