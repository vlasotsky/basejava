package model;

public enum ContactType {
    PHONE_NUMBER("Phone number"),
    MESSENGER_ACCOUNT("Messenger account"),
    EMAIL_ADDRESS("Email address"),
    PERSONAL_WEBPAGE("Personal website"),
    LINKEDIN_ACCOUNT("LinkedIn account"),
    GITHUB_ACCOUNT("Github account"),
    STACKOVERFLOW_ACCOUNT("StackOverflow account");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
