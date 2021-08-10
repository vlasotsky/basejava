package com.basejava.webapp.model;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Resume {
    private final String uuid;
    private final String fullName;

    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, AbstractSection<?>> sections = new EnumMap<>(SectionType.class);

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(fullName, "fullName must not be null");
        Objects.requireNonNull(uuid, "uuid must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
        initializeSections();
    }

    public Map<ContactType, String> getAllContacts() {
        return contacts;
    }

    public Map<SectionType, AbstractSection<?>> getAllSections() {
        return sections;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    private void initializeSections() {
        for (SectionType element : SectionType.values()) {
            this.getAllSections().put(element, null);
        }
    }

    public void saveSection(SectionType type, AbstractSection<?> section) {
        this.getAllSections().put(type, section);
    }

    public void saveContact(ContactType type, String contact) {
        this.getAllContacts().put(type, contact);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resume)) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid) && fullName.equals(resume.fullName) && contacts.equals(resume.contacts) && sections.equals(resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("~Contacts:\n");
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            sb.append(entry.getKey()).append(":\n");
            sb.append(entry.getValue()).append('\n');
        }
        sb.append("_____________________________________").append("\n~Sections:\n");
        for (Map.Entry<SectionType, AbstractSection<?>> entry : sections.entrySet()) {
            sb.append(entry.getKey()).append(":\n");
            sb.append(entry.getValue()).append('\n');
        }
        sb.deleteCharAt(sb.length() - 1);
        return uuid + '(' + fullName + ')' + '\n'
                + sb;
    }

}
