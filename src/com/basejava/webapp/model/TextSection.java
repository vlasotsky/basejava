package com.basejava.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serial;
import java.util.Objects;
import java.util.regex.Pattern;

@XmlAccessorType(XmlAccessType.FIELD)
public class TextSection extends Section {
    @Serial
    private static final long serialVersionUID = 1L;
    public static final Section EMPTY = new TextSection("");

    private String data;

    public TextSection() {
    }

    public TextSection(String description) {
        Objects.requireNonNull(description, "Description must not be null");
        this.data = description;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
//        return '*' + data + '\n';
        return data + '\n';
    }

    private void replaceData(String toBeReplaced) {
        StringBuilder sb = new StringBuilder(data);
        data = Pattern.compile(toBeReplaced).matcher(sb).replaceAll("");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextSection that = (TextSection) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
