package com.basejava.webapp.model;

public abstract class AbstractSection<T> {

    protected abstract void saveToData(T dataNew);

    protected abstract void delete(T dataToDelete);
}
