package com.basejava.webapp.storage.strategy;

import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Link;
import com.basejava.webapp.model.ListSection;
import com.basejava.webapp.model.Organization;
import com.basejava.webapp.model.OrganizationSection;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.model.Section;
import com.basejava.webapp.model.SectionType;
import com.basejava.webapp.model.TextSection;
import com.basejava.webapp.storage.strategy.functionalInterface.SectionReader;
import com.basejava.webapp.storage.strategy.functionalInterface.SectionWriter;

import java.io.*;
import java.time.YearMonth;
import java.util.*;


public class DataStreamSerializer implements StreamSerializer {
    @Override
    public void doWrite(OutputStream outputStream, Resume resume) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());
            Map<ContactType, String> allContacts = resume.getContacts();

            writeWithException(allContacts.entrySet(), dataOutputStream, o -> {
                dataOutputStream.writeUTF(o.getKey().name());
                dataOutputStream.writeUTF(o.getValue());
            });

            Map<SectionType, Section> allSections = resume.getSections();

            Set<Map.Entry<SectionType, Section>> collection = allSections.entrySet();
            writeWithException(collection, dataOutputStream, element -> {
                SectionType sectionType = element.getKey();
                dataOutputStream.writeUTF(sectionType.name());
                Section section = element.getValue();
                switch (sectionType) {
                    case PERSONAL, OBJECTIVE -> writeTextSection(dataOutputStream, (TextSection) section);
                    case QUALIFICATIONS, ACHIEVEMENTS -> writeListSection(dataOutputStream, (ListSection) section);
                    case EXPERIENCE, EDUCATION -> writeOrganisationSection(dataOutputStream, (OrganizationSection) section);
                }
            });
        }
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dataOutputStream, SectionWriter<T> funcInt) throws IOException {
        Objects.requireNonNull(funcInt);
        dataOutputStream.writeInt(collection.size());
        for (T element : collection) {
            funcInt.apply(element);
        }
    }

    private void writeTextSection(DataOutputStream dataOutputStream, TextSection section) throws IOException {
        dataOutputStream.writeUTF(section.getData());
    }

    private void writeListSection(DataOutputStream dataOutputStream, ListSection section) throws IOException {
        List<String> sectionData = section.getData();
        writeWithException(sectionData, dataOutputStream, dataOutputStream::writeUTF);
    }

    private void writeOrganisationSection(DataOutputStream dataOutputStream, OrganizationSection section) throws IOException {
        List<Organization> sectionData = section.getData();
        writeWithException(sectionData, dataOutputStream, organization -> {
            Link link = organization.getLink();
            dataOutputStream.writeUTF(link.getName());
            String url = link.getUrl();
            dataOutputStream.writeUTF(url == null ? "" : url);

            List<Organization.Position> positions = organization.getPositions();
            writeWithException(positions, dataOutputStream, position -> {
                String description = position.getDescription();
                dataOutputStream.writeUTF(position.getStartDate().toString());
                dataOutputStream.writeUTF(position.getEndDate().toString());
                dataOutputStream.writeUTF(position.getTitle());
                dataOutputStream.writeUTF(description == null ? "" : description);
            });
        });
    }

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            String uuid = dataInputStream.readUTF();
            String fullName = dataInputStream.readUTF();
            Resume resume = new Resume(uuid, fullName);

            Map<ContactType, String> allContacts = resume.getContacts();

            readWithException(dataInputStream, () -> allContacts.put(ContactType.valueOf(dataInputStream.readUTF()), dataInputStream.readUTF()));

            Map<SectionType, Section> allSections = resume.getSections();

            readWithException(dataInputStream, () -> {
                SectionType type = SectionType.valueOf(dataInputStream.readUTF());
                switch (type) {
                    case OBJECTIVE, PERSONAL -> allSections.put(type, new TextSection(dataInputStream.readUTF()));
                    case QUALIFICATIONS, ACHIEVEMENTS -> readListSection(dataInputStream, type, allSections);
                    case EXPERIENCE, EDUCATION -> readOrganisationSection(dataInputStream, type, allSections);
                }
            });
            return resume;
        }
    }

    private <T> void readWithException(DataInputStream dataInputStream, SectionReader funcInt) throws IOException {
        Objects.requireNonNull(funcInt);
        int size = dataInputStream.readInt();
        for (int i = 0; i < size; i++) {
            funcInt.apply();
        }
    }

    private void readOrganisationSection(DataInputStream dataInputStream, SectionType sectionType, Map<SectionType, Section> allSections) throws IOException {
        List<Organization> organizationList = new ArrayList<>();

        readWithException(dataInputStream, () -> {
            String name = dataInputStream.readUTF();
            String url = dataInputStream.readUTF();
            Link link = url.isEmpty() ? (new Link(name)) : (new Link(name, url));
            List<Organization.Position> positionList = new ArrayList<>();

            readWithException(dataInputStream, () -> {
                YearMonth startDate = YearMonth.parse(dataInputStream.readUTF());
                YearMonth endDate = YearMonth.parse(dataInputStream.readUTF());
                String title = dataInputStream.readUTF();
                Organization.Position position;

                String description = dataInputStream.readUTF();
                position = description.isEmpty() ? new Organization.Position(startDate, endDate, title) : new Organization.Position(startDate, endDate, title, description);
                positionList.add(position);
            });
            organizationList.add(new Organization(link, positionList));
        });
        allSections.put(sectionType, new OrganizationSection(organizationList));
    }

    private void readListSection(DataInputStream dataInputStream, SectionType sectionType, Map<SectionType, Section> allSections) throws IOException {
        List<String> list = new ArrayList<>();

        readWithException(dataInputStream, ()-> {
            String e = dataInputStream.readUTF();
            list.add(e);
        });
        allSections.put(sectionType, new ListSection(list));
    }
}
