package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.strategy.StreamSerializer;

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
    private final StreamSerializer streamSerializer;

    protected PathStorage(String dir, StreamSerializer streamSerializer) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");

        this.streamSerializer = streamSerializer;
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(dir + " is not a directory");
        } else if (!Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is is not writable");
        }
        Objects.requireNonNull(streamSerializer, "Strategy cannot be null");
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
            throw new StorageException("Path deletion error", getFileName(path));
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return streamSerializer.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Error while reading the path", getFileName(path), e);
        }
    }

    @Override
    protected void doUpdate(Path path, Resume resume) {
        try {
            streamSerializer.doWrite(Files.newOutputStream(path), resume);
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
        return getStreamOfPaths().map(this::doGet).collect(Collectors.toList());
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
            throw new StorageException("Error while reading the directory", e);
        }
    }

    private String getFileName(Path path) {
        return path.getFileName().toString();
    }
}

