package com.basejava.webapp.model;

import java.util.Objects;

public class Link {
    private String name;
    private String link;

    public Link(String name, String link) {
        Objects.requireNonNull(name, "Organisation name must not be null");
        this.name = name;
        this.link = link;
    }

    public Link(String name) {
        Objects.requireNonNull(name, "Organisation name must not be null");
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Organisation: ").append(name).append('\n');
        if (link != null) {
            sb.append("Link: ").append(link);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Link)) return false;
        Link link1 = (Link) o;
        return Objects.equals(name, link1.name) && Objects.equals(link, link1.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, link);
    }
}
