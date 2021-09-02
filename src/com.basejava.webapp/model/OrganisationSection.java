package com.basejava.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrganisationSection extends Section<Organisation> {
    private static final long serialVersionUID = 1L;
    private List<Organisation> data = new ArrayList<>();

    public OrganisationSection() {
    }

    public OrganisationSection(Organisation... organisations) {
        this(Arrays.asList(organisations));
    }

    public OrganisationSection(List<Organisation> organisations) {
        Objects.requireNonNull(organisations, "Data must not be null");
        this.data = organisations;
    }

    public List<Organisation> getData() {
        return data;
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

