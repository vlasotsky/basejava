package model;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Resume {
    private final String uuid;
    private final String fullName;

    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final EnumMap<SectionType, AbstractSection<?>> sections = new EnumMap<>(SectionType.class);

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

    public EnumMap<SectionType, AbstractSection<?>> getAllSections() {
        return sections;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void printAllContacts() {
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("~Contacts:\n");
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            stringBuilder.append(entry.getKey()).append(":\n");
            stringBuilder.append(entry.getValue()).append('\n');
        }
        stringBuilder.append("_____________________________________").append("\n~Sections:\n");
        for (Map.Entry<SectionType, AbstractSection<?>> entry : sections.entrySet()) {
            stringBuilder.append(entry.getKey()).append(":\n");
            stringBuilder.append(entry.getValue()).append('\n');
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return uuid + '(' + fullName + ')' + '\n'
                + stringBuilder;
    }

}
