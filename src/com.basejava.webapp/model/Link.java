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
}
