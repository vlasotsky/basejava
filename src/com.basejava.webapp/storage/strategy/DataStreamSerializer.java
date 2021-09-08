package com.basejava.webapp.storage.strategy;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Link;
import com.basejava.webapp.model.ListSection;
import com.basejava.webapp.model.Organization;
import com.basejava.webapp.model.OrganizationSection;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.model.Section;
import com.basejava.webapp.model.SectionType;
import com.basejava.webapp.model.TextSection;

import java.io.*;
import java.time.YearMonth;
import java.util.*;


public class DataStreamSerializer implements StreamSerializer {
    @Override
    public void doWrite(OutputStream outputStream, Resume resume) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());
            Map<ContactType, String> allContacts = resume.getAllContacts();
            dataOutputStream.writeInt(allContacts.size());

            writeWithException(allContacts.entrySet(), o -> {
                dataOutputStream.writeUTF(o.getKey().name());
                dataOutputStream.writeUTF(o.getValue());
            });

            Map<SectionType, Section> allSections = resume.getAllSections();
            dataOutputStream.writeInt(allSections.size());

            Set<Map.Entry<SectionType, Section>> collection = allSections.entrySet();
            writeWithException(collection, element -> {
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

    private <T> void writeWithException(Collection<T> collection, MyFunctionalInterface<T> funcInt) throws IOException {
        Objects.requireNonNull(funcInt);
        for (T element : collection) {
            funcInt.apply(element);
        }
    }

    private void writeTextSection(DataOutputStream dataOutputStream, TextSection section) {
        try {
            dataOutputStream.writeUTF(section.getDescription());
        } catch (IOException e) {
            throw new StorageException("Error while writing a TextSection", e);
        }
    }

    private void writeListSection(DataOutputStream dataOutputStream, ListSection section) throws IOException {
        int size = section.getData().size();
        dataOutputStream.writeInt(size);

        List<String> data = section.getData();
        for (int i = 0; i < size; i++) {
            dataOutputStream.writeUTF(data.get(i));
        }
    }

    private void writeOrganisationSection(DataOutputStream dataOutputStream, OrganizationSection section) throws IOException {
        int numberOfOrganisations = section.getData().size();
        dataOutputStream.writeInt(numberOfOrganisations);

        for (Organization element : section.getData()) {
            Link link = element.getLink();
            dataOutputStream.writeUTF(link.getName());
            if (link.getUrl() == null) {
                dataOutputStream.writeUTF("");
            } else {
                dataOutputStream.writeUTF(link.getUrl());
            }
            dataOutputStream.writeInt(element.getPositions().size());
            for (Organization.Position position : element.getPositions()) {
                dataOutputStream.writeUTF(position.getStartDate().toString());
                dataOutputStream.writeUTF(position.getEndDate().toString());
                dataOutputStream.writeUTF(position.getTitle());
                dataOutputStream.writeUTF(position.getDescription() == null ? "" : position.getDescription());
            }
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            String uuid = dataInputStream.readUTF();
            String fullName = dataInputStream.readUTF();
            Resume resume = new Resume(uuid, fullName);
            Map<ContactType, String> allContacts = resume.getAllContacts();
            int numContacts = dataInputStream.readInt();
            for (int i = 0; i < numContacts; i++) {
                allContacts.put(ContactType.valueOf(dataInputStream.readUTF()), dataInputStream.readUTF());
            }
            int numSections = dataInputStream.readInt();
            Map<SectionType, Section> allSections = resume.getAllSections();
            for (int i = 0; i < numSections; i++) {
                SectionType type = SectionType.valueOf(dataInputStream.readUTF());
                switch (type) {
                    case OBJECTIVE, PERSONAL -> allSections.put(type, new TextSection(dataInputStream.readUTF()));
                    case QUALIFICATIONS, ACHIEVEMENTS -> readListSection(dataInputStream, type, resume);
                    case EXPERIENCE, EDUCATION -> readOrganisationSection(dataInputStream, type, resume);
                }
            }
            return resume;
        }
    }

    private void readOrganisationSection(DataInputStream dataInputStream, SectionType sectionType, Resume resume) throws IOException {
        int numberOfOrganizations = dataInputStream.readInt();
        List<Organization> organizationList = new ArrayList<>();
        for (int j = 0; j < numberOfOrganizations; j++) {
            String name = dataInputStream.readUTF();
            Link link;
            String url = dataInputStream.readUTF();
            if (url.isEmpty()) {
                link = new Link(name);
            } else {
                link = new Link(name, url);
            }
            List<Organization.Position> positionList = new ArrayList<>();
            int numPositions = dataInputStream.readInt();
            for (int k = 0; k < numPositions; k++) {
                YearMonth startDate = YearMonth.parse(dataInputStream.readUTF());
                YearMonth endDate = YearMonth.parse(dataInputStream.readUTF());
                String title = dataInputStream.readUTF();
                Organization.Position position;

                String description = dataInputStream.readUTF();
                position = description.isEmpty() ? new Organization.Position(startDate, endDate, title) : new Organization.Position(startDate, endDate, title, description);
                positionList.add(position);
            }
            organizationList.add(new Organization(link, positionList));
        }
        resume.getAllSections().put(sectionType, new OrganizationSection(organizationList));
    }

    private void readListSection(DataInputStream dataInputStream, SectionType sectionType, Resume resume) throws IOException {
        int size = dataInputStream.readInt();
        List<String> list = new ArrayList<>();
        for (int j = 0; j < size; j++) {
            String e = dataInputStream.readUTF();
            list.add(e);
        }
        resume.getAllSections().put(sectionType, new ListSection(list));
    }
}
