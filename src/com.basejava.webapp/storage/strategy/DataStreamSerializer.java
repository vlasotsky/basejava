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

import java.io.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {
    @Override
    public void doWrite(OutputStream outputStream, Resume resume) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());
            Map<ContactType, String> allContacts = resume.getAllContacts();
            dataOutputStream.writeInt(allContacts.size());
            for (Map.Entry<ContactType, String> entry : allContacts.entrySet()) {
                dataOutputStream.writeUTF(entry.getKey().name());
                dataOutputStream.writeUTF(entry.getValue());
            }

            Map<SectionType, Section> allSections = resume.getAllSections();
            dataOutputStream.writeInt(allSections.size());

            for (Map.Entry<SectionType, Section> entry : allSections.entrySet()) {
                switch (entry.getKey()) {
                    case PERSONAL -> writeTextSection(dataOutputStream, SectionType.PERSONAL, resume);
                    case OBJECTIVE -> writeTextSection(dataOutputStream, SectionType.OBJECTIVE, resume);
                    case QUALIFICATIONS -> writeListSection(dataOutputStream, SectionType.QUALIFICATIONS, resume);
                    case ACHIEVEMENTS -> writeListSection(dataOutputStream, SectionType.ACHIEVEMENTS, resume);
                    case EXPERIENCE -> writeOrganisationSection(dataOutputStream, SectionType.EXPERIENCE, resume);
                    case EDUCATION -> writeOrganisationSection(dataOutputStream, SectionType.EDUCATION, resume);
                }
            }
        }
    }

    private void writeTextSection(DataOutputStream dataOutputStream, SectionType sectionType, Resume resume) {
        try {
            dataOutputStream.writeUTF(sectionType.name());
            TextSection textSection = (TextSection) resume.getAllSections().get(sectionType);
            dataOutputStream.writeUTF(textSection.getDescription());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeListSection(DataOutputStream dataOutputStream, SectionType sectionType, Resume resume) {
        try {
            dataOutputStream.writeUTF(sectionType.name());
            ListSection listSection = (ListSection) resume.getAllSections().get(sectionType);
            int size = listSection.getData().size();
            dataOutputStream.writeInt(size);

            List<String> data = listSection.getData();
            for (int i = 0; i < size; i++) {
                dataOutputStream.writeUTF(data.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeOrganisationSection(DataOutputStream dataOutputStream, SectionType sectionType, Resume resume) {
        try {
            dataOutputStream.writeUTF(sectionType.name());
            OrganizationSection organizationSection = (OrganizationSection) resume.getAllSections().get(sectionType);
            int numberOfOrganisations = organizationSection.getData().size();
            dataOutputStream.writeInt(numberOfOrganisations);

            for (Organization element : organizationSection.getData()) {
                Link link = element.getLink();
                dataOutputStream.writeUTF(link.getName());
                if (link.getUrl() != null) {
                    dataOutputStream.writeBoolean(true);
                    dataOutputStream.writeUTF(link.getUrl());
                } else {
                    dataOutputStream.writeBoolean(false);
                }
                dataOutputStream.writeInt(element.getPositions().size());
                for (Organization.Position position : element.getPositions()) {
                    dataOutputStream.writeUTF(position.getStartDate().toString());
                    dataOutputStream.writeUTF(position.getEndDate().toString());
                    dataOutputStream.writeUTF(position.getTitle());
                    if (sectionType.equals(SectionType.EXPERIENCE)) {
                        dataOutputStream.writeUTF(position.getDescription());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                switch (SectionType.valueOf(dataInputStream.readUTF())) {
                    case OBJECTIVE -> allSections.put(SectionType.OBJECTIVE, new TextSection(dataInputStream.readUTF()));
                    case PERSONAL -> allSections.put(SectionType.PERSONAL, new TextSection(dataInputStream.readUTF()));
                    case QUALIFICATIONS -> readListSection(dataInputStream, SectionType.QUALIFICATIONS, resume);
                    case ACHIEVEMENTS -> readListSection(dataInputStream, SectionType.ACHIEVEMENTS, resume);
                    case EXPERIENCE -> readOrganisationSection(dataInputStream, SectionType.EXPERIENCE, resume);
                    case EDUCATION -> readOrganisationSection(dataInputStream, SectionType.EDUCATION, resume);
                }
            }
            return resume;
        }
    }

    private void readOrganisationSection(DataInputStream dataInputStream, SectionType sectionType, Resume resume) {
        try {
            int numberOfOrganizations = dataInputStream.readInt();
            List<Organization> organizationList = new ArrayList<>();
            for (int j = 0; j < numberOfOrganizations; j++) {
                String name = dataInputStream.readUTF();
                boolean urlExists = dataInputStream.readBoolean();
                Link link;
                if (urlExists) {
                    String url = dataInputStream.readUTF();
                    link = new Link(name, url);
                } else {
                    link = new Link(name);
                }
                List<Organization.Position> positionList = new ArrayList<>();
                int numPositions = dataInputStream.readInt();
                for (int k = 0; k < numPositions; k++) {
                    YearMonth startDate = YearMonth.parse(dataInputStream.readUTF());
                    YearMonth endDate = YearMonth.parse(dataInputStream.readUTF());
                    String title = dataInputStream.readUTF();
                    Organization.Position position;
                    if (sectionType.equals(SectionType.EXPERIENCE)) {
                        String description = dataInputStream.readUTF();
                        position = new Organization.Position(startDate, endDate, title, description);
                    } else {
                        position = new Organization.Position(startDate, endDate, title);
                    }
                    positionList.add(position);
                }
                organizationList.add(new Organization(link, positionList));
            }
            resume.getAllSections().put(sectionType, new OrganizationSection(organizationList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readListSection(DataInputStream dataInputStream, SectionType sectionType, Resume resume) {
        try {
            int size = dataInputStream.readInt();
            List<String> list = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                String e = dataInputStream.readUTF();
                list.add(e);
            }
            resume.getAllSections().put(sectionType, new ListSection(list));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
