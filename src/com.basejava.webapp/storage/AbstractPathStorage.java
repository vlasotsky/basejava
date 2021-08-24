package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.*;
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
            throw new IllegalArgumentException(dir + "is not a directory or it is not writable");
        }
    }

    protected abstract void doWrite(OutputStream outputStream, Resume resume) throws IOException;

    protected abstract Resume doRead(InputStream inputStream) throws IOException;

    @Override
    protected Path findSearchKey(String uuid) {
//        return Paths.get(directory.forEach(x-> {
//            if (x.getFileName().toString().equals(uuid)) {
//                try {
//                    return x.toRealPath();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }));
        for (Path element : directory) {
            if (element.getFileName().toString().equals(uuid)) {
                return element;
            }
        }
        return null;
    }

    @Override
    protected void doSave(Path path, Resume resume) {
//            Files.copy(path, directory);
        File file = new File(path + "\\" + resume.getUuid());
//        } catch (IOException e) {
//            throw new StorageException("Couldn't create Path " + path.toAbsolutePath(), path.getFileName().toString(), e);
//        }
//        doUpdate(path, resume);
        doUpdate(file.toPath(), resume);
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
//            doWrite(new BufferedOutputStream(new FileOutputStream(path.toFile())), resume);
            doWrite(new BufferedOutputStream(new FileOutputStream(path + "\\" + resume.getUuid())), resume);
        } catch (IOException e) {
            throw new StorageException("Error while writing the path", resume.getUuid(), e);
        }
    }


    @Override
    protected boolean isNotExisting(Path path) {
        return path == null;
//        return Files.notExists(path);
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
        int size = directory.getNameCount();
        if (size == 0) {
            throw new StorageException("Error in attempt to read the directory or it is empty", null);
        }
        return size;
    }
// OR:
//        try {
//            return (int)Files.getAttribute(directory, "size");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        throw new StorageException("Error with calculating the size of a directory", null);
//    }
}

