package model;

public abstract class AbstractSection<T> {

    protected abstract void printData();

    protected abstract void saveToData(T dataNew);

    protected abstract void update(T dataPrev, T dataNew);

    protected abstract void delete(T dataToDelete);
}
