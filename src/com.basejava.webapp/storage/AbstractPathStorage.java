package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    private Path directory;

    protected AbstractPathStorage(String dir) {
        Objects.requireNonNull(dir, "directory must not be null");
        directory = Paths.get(dir);
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + "is not a directory or it is not writable");
        }
    }

    protected abstract void doWrite(OutputStream outputStream, Resume resume) throws IOException;

    protected abstract Resume doRead(BufferedInputStream inputStream) throws IOException;

    @Override
    protected Path findSearchKey(String uuid) {
        return Paths.get(directory.toUri());
    }

    @Override
    protected void doSave(Path path, Resume resume) {
//        try {
//            path.createNewPath();
//        } catch (IOException e) {
//            throw new StorageException("Couldn't create Path " + directory.toAbsolutePath(), directory.getFileName().toString(), e);
//        }
//        doUpdate(path, resume);
    }


    @Override
    protected void doDelete(Path path) {
//        if (!path.delete()) {
//            throw new StorageException("Path deletion error", path.getFileName().toString());
//        }
    }

    @Override
    protected Resume doGet(Path path) {
//        try {
//            return doRead(new BufferedInputStream(new PathInputStream(path)));
//        } catch (IOException e) {
//            throw new StorageException("Error while reading a Path", path.getFileName().toString(), e);
//        }
        return null;
    }

    @Override
    protected void doUpdate(Path Path, Resume resume) {
//        try {
//            doWrite(new BufferedOutputStream(new PathOutputStream(Path)));
//        } catch (IOException e) {
//            throw new StorageException("Error while writing into a Path", resume.getUuid(), e);
//        }
    }


    @Override
    protected boolean isNotExisting(Path path) {
        return new File(String.valueOf(path)).exists();
    }

    @Override
    protected List<Resume> doCopyAll() {
//        Path[] paths = directory.listPaths();
//        if (paths == null) {
//            throw new StorageException("Error while reading a Path", null);
//        }
//        List<Resume> list = new ArrayList<>(paths.length);
//        for (Path Path : paths) {
//            list.add(doGet(Path));
//        }
//        return list;
        return null;
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    public int size() {
//        String[] list = directory.list();
//        if (list == null) {
//            throw new StorageException("Error in attempt to read the directory", null);
//        }
//        return list.length;
        return 0;
    }
}
