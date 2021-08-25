package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

public interface Strategy {
    void doAction(Resume resume);

    Object getStorage();

    Object getObject();
}
