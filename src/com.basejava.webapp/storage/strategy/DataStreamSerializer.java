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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            Map<SectionType, Section<?>> allSections = resume.getAllSections();
            dataOutputStream.writeInt(allSections.size());
            for (Map.Entry<SectionType, Section<?>> entry : allSections.entrySet()) {
                dataOutputStream.writeUTF(entry.getKey().name());
                dataOutputStream.writeUTF(String.valueOf(entry.getValue()));
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
            Map<SectionType, Section<?>> allSections = resume.getAllSections();
            for (int i = 0; i < numSections; i++) {
                String readData = dataInputStream.readUTF();
                switch (readData) {
                    case "OBJECTIVE" -> allSections.put(SectionType.OBJECTIVE, new TextSection(removeEmbellishments(dataInputStream.readUTF())));
                    case "QUALIFICATIONS" -> saveStringSection(resume, SectionType.QUALIFICATIONS, new ListSection(getListSection(dataInputStream.readUTF())));
                    case "PERSONAL" -> saveStringSection(resume, SectionType.PERSONAL, new TextSection(removeEmbellishments(dataInputStream.readUTF())));
                    case "ACHIEVEMENTS" -> allSections.put(SectionType.ACHIEVEMENTS, new ListSection(getListSection(dataInputStream.readUTF())));
                    case "EXPERIENCE" -> {
                        String[] data = getDataFromString(dataInputStream.readUTF());

                        List<Organization> organizationList = new ArrayList<>();
                        for (String element : data) {
                            organizationList.add(getExperienceOrganisation(element));
                        }
                        saveStringSection(resume, SectionType.EXPERIENCE, new OrganizationSection(organizationList));
                    }
                    case "EDUCATION" -> {
                        String[] data = getDataFromString(dataInputStream.readUTF());

                        List<Organization> organizationList = new ArrayList<>();
                        for (String element : data) {
                            organizationList.add(getEducationOrganisation(element));
                        }
                        saveStringSection(resume, SectionType.EDUCATION, new OrganizationSection(organizationList));
                    }
                }
            }
            return resume;
        }
    }

    private String[] getDataFromString(String data) {
        return data.split("\n\n");
    }

    private String[] getListSection(String data) {
        String[] arr = data.split("\n");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].substring(1);
        }
        return arr;
    }

    private Organization getExperienceOrganisation(String data) {
        String[] arr = data.split("\n");

        String organisation = arr[0].replaceAll("Organisation: ", "");
        if (arr.length == 5) {
            String link = arr[1].replaceAll("Link: ", "");
            int[] period = findDates(arr[2]);
            String title = arr[3].replaceAll("Title: ", "");
            String description = arr[4].replaceAll("Description: ", "");
            return new Organization(organisation, link, new Organization.Position(period[0], period[1], period[2], period[3], title, description));
        } else {
            int[] period = findDates(arr[1]);
            String title = arr[2].replaceAll("Title: ", "");
            String description = arr[3].replaceAll("Description: ", "");
            return new Organization(new Link(organisation), new Organization.Position(period[0], period[1], period[2], period[3], title, description));
        }
    }

    private Organization getEducationOrganisation(String data) {
        String[] arr = data.split("\n");
        String organisation = arr[0].replaceAll("Organisation: ", "");
        String link = arr[1].replaceAll("Link: ", "");

        List<Organization.Position> list = new ArrayList<>();
        for (int i = 3, j = i - 1; i < arr.length; i += 2) {
            int[] period = findDates(arr[j].replaceAll("Period: ", ""));
            String title = arr[i].replaceAll("Title: ", "");
            list.add(new Organization.Position(period[0], period[1], period[2], period[3], title));
            j += 2;
        }
        return new Organization(new Link(organisation, link), list);
    }

    private int[] findDates(String period) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(period);
        int[] arr = new int[4];
        int i = 0;
        while (matcher.find()) {
            arr[i] = Integer.parseInt(matcher.group());
            i++;
        }
        return arr;
    }

    private void saveStringSection(Resume resume, SectionType type, Section<?> section) {
        resume.getAllSections().put(type, section);
    }

    private String removeEmbellishments(String data) {
        return data.substring(1, data.length() - 1);
    }
}
