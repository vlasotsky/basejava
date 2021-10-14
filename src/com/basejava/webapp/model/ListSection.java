package com.basejava.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class ListSection extends Section {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<String> data = new ArrayList<>();

    public ListSection() {
    }

    public ListSection(List<String> data) {
        Objects.requireNonNull(data, "Data must not be null");
        this.data = data;
    }

    public ListSection(String... data) {
        this(Arrays.asList(data));
    }


    public List<String> getData() {
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String element : data) {
//            sb.append('*').append(element).append('\n');
            sb.append(element).append('\n');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
