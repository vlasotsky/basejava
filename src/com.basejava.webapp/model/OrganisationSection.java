package com.basejava.webapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrganisationSection extends AbstractSection<Organisation> {
    private static final long serialVersionUID = 1L;

    private final List<Organisation> data;

    public OrganisationSection(Organisation... organisations) {
        this(Arrays.asList(organisations));
    }

    public OrganisationSection(List<Organisation> organisations) {
        Objects.requireNonNull(organisations, "Data must not be null");
        this.data = organisations;
    }

    public OrganisationSection() {
        this.data = new ArrayList<>();
    }

    public List<Organisation> getData() {
        return data;
    }

    @Override
    protected void saveToData(Organisation dataNew) {
        data.add(dataNew);
    }

    @Override
    protected void delete(Organisation dataToDelete) {
        data.remove(dataToDelete);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrganisationSection)) return false;
        OrganisationSection that = (OrganisationSection) o;
        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        StringBuilder dataText = new StringBuilder();
        for (Organisation element : data) {
            dataText.append(element).append('\n');
        }
        dataText.deleteCharAt(dataText.length() - 1);
        return dataText.toString();
    }
}
