package com.basejava.webapp.model;

import java.util.Objects;
import java.util.regex.Pattern;

public class StringSection extends AbstractSection<String> {
    private String description;

    public StringSection(String description) {
        Objects.requireNonNull(description, "Description must not be null");
        this.description = description;
    }

    @Override
    protected void printData() {
        System.out.println(description);
    }

    @Override
    protected void saveToData(String dataNew) {
        description += ' ' + dataNew;
    }

    @Override
    protected void update(String dataPrev, String dataNew) {
        replaceData(dataPrev, dataNew);
        System.out.println("Required data was deleted");
    }

    @Override
    protected void delete(String dataToDelete) {
        replaceData(dataToDelete, "");
        System.out.println("Section was updated");
    }

    @Override
    public String toString() {
        return '*' + description + '\n';
    }

    private void replaceData(String toBeReplaced, String replacement) {
        StringBuilder sb = new StringBuilder(description);
        description = Pattern.compile(toBeReplaced).matcher(sb).replaceAll(replacement);
    }
}
