package com.basejava.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Objects;
import java.util.regex.Pattern;

@XmlAccessorType(XmlAccessType.FIELD)
public class TextSection extends Section<String> {
    private static final long serialVersionUID = 1L;

    private String description;

    public TextSection() {
    }

    public TextSection(String description) {
        Objects.requireNonNull(description, "Description must not be null");
        this.description = description;
    }

    @Override
    public String toString() {
        return '*' + description + '\n';
    }

    private void replaceData(String toBeReplaced) {
        StringBuilder sb = new StringBuilder(description);
        description = Pattern.compile(toBeReplaced).matcher(sb).replaceAll("");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextSection)) return false;
        TextSection that = (TextSection) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
