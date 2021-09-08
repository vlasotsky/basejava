package com.basejava.webapp.storage.strategy;

import java.io.IOException;
@FunctionalInterface
public interface MyFunctionalInterface<T> {
    void apply(T t) throws IOException;
}
