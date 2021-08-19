package com.basejava.webapp.model;

import java.io.Serializable;
import java.util.Objects;

public class Link implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private String url;

    public Link(String name, String url) {
        Objects.requireNonNull(name, "Organisation name must not be null");
        this.name = name;
        this.url = url;
    }

    public Link(String name) {
        Objects.requireNonNull(name, "Organisation name must not be null");
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Organisation: ").append(name).append('\n');
        if (url != null) {
            sb.append("Link: ").append(url);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Link)) return false;
        Link link1 = (Link) o;
        return Objects.equals(name, link1.name) && Objects.equals(url, link1.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }
}
