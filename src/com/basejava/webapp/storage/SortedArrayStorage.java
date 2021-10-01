package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;
import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    private static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getUuid);

    protected Integer findSearchKey(String uuid) {
        return Arrays.binarySearch(storage, 0, size, new Resume(uuid, "dummy"), RESUME_COMPARATOR);
    }

    @Override
    protected void saveToArray(int foundIndex, Resume resume) {
        int neededIndex = Math.abs(foundIndex) - 1;
        System.arraycopy(storage, neededIndex, storage, neededIndex + 1, size - neededIndex);
        storage[neededIndex] = resume;
    }
}

