package com.basejava.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Serializable {
    private static final long serialVersionUID = 1L;

    private String uuid;
    private String fullName;

    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, Section<?>> sections = new EnumMap<>(SectionType.class);

    public Resume() {
    }

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

    public Map<SectionType, Section<?>> getAllSections() {
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

    public void saveSection(SectionType type, Section<?> section) {
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
        return Objects.equals(uuid, resume.uuid) && Objects.equals(fullName, resume.fullName) && Objects.equals(contacts, resume.contacts) && Objects.equals(sections, resume.sections);
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
        for (Map.Entry<SectionType, Section<?>> entry : sections.entrySet()) {
            sb.append(entry.getKey()).append(":\n");
            sb.append(entry.getValue()).append('\n');
        }
        sb.deleteCharAt(sb.length() - 1);
        return uuid + '(' + fullName + ')' + '\n'
                + sb;
    }

}
