package com.basejava.webapp.model;

public enum ContactType {
    PHONE_NUMBER("Phone number"),
    MESSENGER_ACCOUNT("Messenger account") {
        @Override
        public String toHtml0(String value) {
            return "<a href='messenger:" + value + "'>" + value + "</a>";
        }
    },
    EMAIL_ADDRESS("Email address") {
        @Override
        public String toHtml0(String value) {
            return "<a href='mailto:" + value + "'>" + value + "</a>";
        }
    },
    PERSONAL_WEBPAGE("Personal webpage"),
    LINKEDIN_ACCOUNT("LinkedIn account"),
    GITHUB_ACCOUNT("Github account"),
    STACKOVERFLOW_ACCOUNT("StackOverflow account");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    protected String toHtml0(String value) {
        return title + ": " + value;
    }

    public String toHtml(String value) {
//        return (value == null) ? "" : title + ": " + toHtml0(value);
        return (value == null) ? "" : toHtml0(value);
    }
}
