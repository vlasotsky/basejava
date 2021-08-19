package com.basejava.webapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListSection extends AbstractSection<String> {
    private static final long serialVersionUID = 1L;

    private final List<String> data;

    public ListSection(List<String> data) {
        Objects.requireNonNull(data, "Data must not be null");
        this.data = data;
    }

    public ListSection(String... data) {
        this(Arrays.asList(data));
    }

    public ListSection() {
        this.data = new ArrayList<>();
    }

    @Override
    protected void saveToData(String dataNew) {
        data.add(dataNew);
    }

    @Override
    protected void delete(String dataToDelete) {
        this.data.remove(dataToDelete);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String element : data) {
            sb.append('*').append(element).append('\n');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListSection)) return false;
        ListSection that = (ListSection) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
