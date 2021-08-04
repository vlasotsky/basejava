package model;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Resume {
    private final String uuid;
    private final String fullName;

    private final EnumMap<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final EnumMap<SectionType, AbstractSection<?, ?>> sections = new EnumMap<>(SectionType.class);

    //constructors:
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

    //getters:
    public Map<ContactType, String> getAllContacts() {
        return contacts;
    }

    public String getContact(ContactType type) {
        return this.getAllContacts().get(type);
    }

    public AbstractSection<?, ?> getSection(SectionType sectionType) {
        return this.getAllSections().get(sectionType);
    }

    public EnumMap<SectionType, AbstractSection<?, ?>> getAllSections() {
        return sections;
    }


    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        if (!uuid.equals(resume.uuid)) {
            return false;
        }
        return fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return uuid + '(' + fullName + ')';
    }

    public void printContact(ContactType contactType) {
        System.out.println(this.getAllContacts().get(contactType));
    }

    public void printSection(SectionType sectionType) {
        this.getAllSections().get(sectionType).printData();
    }

    private void initializeSections() {
        for (SectionType element : SectionType.values()) {
            this.getAllSections().put(element, null);
        }
    }

    public void saveSection(SectionType type, AbstractSection<?, ?> section) {
        this.getAllSections().put(type, section);
    }

    public void saveContact(ContactType type, String contact) {
        this.getAllContacts().put(type, contact);
    }

    public void printAllContacts() {
        System.out.println();
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public void printAllSections() {
        System.out.println();
        for (Map.Entry<SectionType, AbstractSection<?, ?>> entry : sections.entrySet()) {
            System.out.print(entry.getKey() + ":" + '\n');
            System.out.println(entry.getValue());
            System.out.println("________________________________________________________________________");
        }
    }
}
