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

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Organisation: ").append(name).append('\n');
//        sb.append("Organisation: ").append(name);
        if (link != null) {
//            sb.append("Link: ").append(link).append('\n');
            sb.append("Link: ").append(link);
        }
        return sb.toString();
    }
}
