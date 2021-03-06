package com.basejava.webapp.model;

public enum SectionType {
    OBJECTIVE("Current occupation"),
    PERSONAL("Personal qualities"),

    ACHIEVEMENTS("Achievements"),
    QUALIFICATIONS("Qualifications"),

    EXPERIENCE("Work experience"),
    EDUCATION("Education");

    private final String title;

    SectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
