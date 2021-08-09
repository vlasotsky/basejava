package storage;

import exception.StorageException;
import model.Resume;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not a directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected File findSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doSave(File file, Resume resume) {
        try {
            file.createNewFile();
            doWrite(file, resume);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    protected abstract void doWrite(File file, Resume resume) throws IOException;

    @Override
    protected void doDelete(File file) {
        File[] files = Objects.requireNonNull(directory.listFiles());
        for (File element: files) {
            if (element == file) {
                element.delete();
            }
        }
    }

    @Override
    protected Resume doGet(File file) {
        //next lesson's material
        doRead();
        return null;
    }

    protected abstract void doRead();

    @Override
    protected void doUpdate(File file, Resume resume) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(resume);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected boolean isNotExisting(File file) {
        return !file.exists();
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> list = new ArrayList<>();
        try (ObjectInputStream fis = new ObjectInputStream(new FileInputStream(directory))) {
            while (fis.available() != 0) {
                list.add((Resume) fis.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new StorageException("IO error| ClassNotFound error", e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        assert files != null;
        for (File element : files) {
            element.delete();
        }
        System.out.println("Files were deleted");
    }

    @Override
    public int size() {
        return Objects.requireNonNull(directory.list()).length;
    }
}
