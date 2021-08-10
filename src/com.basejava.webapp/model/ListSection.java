package com.basejava.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class ListSection extends AbstractSection<String> {
    private final List<String> data = new ArrayList<>();

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
}