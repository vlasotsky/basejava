package com.basejava.webapp.storage.strategy;

import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.ListSection;
import com.basejava.webapp.model.Organisation;
import com.basejava.webapp.model.OrganisationSection;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.model.Section;
import com.basejava.webapp.model.SectionType;
import com.basejava.webapp.model.TextSection;

import java.io.*;
import java.time.YearMonth;
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
            // TODO implement sections
            //anonymous classes, lambdas, maximum code reduction
            Map<SectionType, Section<?>> allSections = resume.getAllSections();
            dataOutputStream.writeInt(allSections.size());
            for (Map.Entry<SectionType, Section<?>> entry : allSections.entrySet()) {
                dataOutputStream.writeUTF(entry.getKey().name());
                dataOutputStream.writeUTF(String.valueOf(entry.getValue()));
            }
        }
    }

    @Deprecated
    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            String uuid = dataInputStream.readUTF();
            String fullName = dataInputStream.readUTF();
            Resume resume = new Resume(uuid, fullName);
            Map<ContactType, String> allContacts = resume.getAllContacts();
            int size = dataInputStream.readInt();
            for (int i = 0; i < size; i++) {
                allContacts.put(ContactType.valueOf(dataInputStream.readUTF()), dataInputStream.readUTF());
            }
            Map<SectionType, Section<?>> allSections = resume.getAllSections();
            for (int i = 0; i < 6; i++) {
                String readData = dataInputStream.readUTF();
                switch (readData) {
                    case "OBJECTIVE" -> allSections.put(SectionType.OBJECTIVE, new TextSection(dataInputStream.readUTF()));
                    case "QUALIFICATIONS" -> allSections.put(SectionType.QUALIFICATIONS, new ListSection(dataInputStream.readUTF()));
                    case "PERSONAL" -> allSections.put(SectionType.PERSONAL, new TextSection(dataInputStream.readUTF()));
                    case "ACHIEVEMENTS" -> allSections.put(SectionType.ACHIEVEMENTS, new ListSection(dataInputStream.readUTF()));
                    case "EXPERIENCE" -> allSections.put(SectionType.EXPERIENCE, new OrganisationSection(
                            new Organisation(dataInputStream.readUTF(), dataInputStream.readUTF(), new Organisation.Position(YearMonth.parse(dataInputStream.readUTF()), YearMonth.parse(dataInputStream.readUTF()), dataInputStream.readUTF(), dataInputStream.readUTF()))));
                    case "EDUCATION" -> allSections.put(SectionType.EDUCATION, new OrganisationSection());
                }
            }
            // TODO implement sections
            //anonymous classes, lambdas, maximum code reduction
            return resume;
        }
    }
}
