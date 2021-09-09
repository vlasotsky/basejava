package com.basejava.webapp.storage.strategy.functionalInterface;

import java.io.IOException;
@FunctionalInterface
public interface SectionWriter<T> {
    void apply(T t) throws IOException;
}
