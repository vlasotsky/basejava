package com.basejava.webapp.model;

import java.io.Serializable;

public abstract class AbstractSection<T>  implements Serializable {

    protected abstract void saveToData(T dataNew);

    protected abstract void delete(T dataToDelete);
}
