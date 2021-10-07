package com.basejava.webapp.storage;

import com.basejava.webapp.exception.NotExistingStorageException;
import com.basejava.webapp.model.*;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", (PreparedStatement::execute));
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                        "SELECT r.uuid, r.full_name, c.type AS type_contact, c.value AS value_contact, s.type AS type_section, s.value AS value_section " +
                        "FROM resume r " +
                        "LEFT JOIN contact c " +
                        "ON r.uuid = c.resume_uuid " +
                        "LEFT JOIN section s " +
                        "ON r.uuid = s.resume_uuid " +
                        "WHERE r.uuid =?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistingStorageException(uuid);
                    }
                    Resume resume = new Resume(uuid, rs.getString("full_name"));
                    do {
                        addContactAndSection(rs, resume);
                    } while (rs.next());
                    return resume;
                });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(connection -> {
            String uuid = resume.getUuid();
            try (PreparedStatement ps = connection.prepareStatement("" +
                    "UPDATE resume " +
                    "SET full_name = ? " +
                    "WHERE uuid = ?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, uuid);
                if (ps.executeUpdate() == 0) {
                    throw new NotExistingStorageException(uuid);
                }
            }
            deleteContactsAndSections(resume);
            insertContactsAndSections(resume, connection);
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(
                connection -> {
                    try (PreparedStatement ps = connection.prepareStatement("" +
                            "INSERT INTO resume(full_name, uuid) " +
                            "VALUES (?, ?)")) {
                        ps.setString(1, resume.getFullName());
                        ps.setString(2, resume.getUuid());
                        ps.execute();
                        insertContactsAndSections(resume, connection);
                    }
                    return null;
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("" +
                "DELETE FROM resume " +
                "WHERE uuid=?", ps -> {
            ps.setString(1, uuid);
            if (!ps.execute()) {
                throw new NotExistingStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute("" +
                "SELECT COUNT(r.uuid) " +
                "FROM resume r", ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("" +
                        "SELECT r.uuid, r.full_name, c.type AS type_contact, c.value AS value_contact, s.type AS type_section, s.value AS value_section " +
                        "FROM resume r " +
                        "LEFT JOIN contact c ON (r.uuid = c.resume_uuid) " +
                        "LEFT JOIN section s ON (r.uuid = s.resume_uuid) " +
                        "ORDER BY r.full_name, r.uuid",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    Map<String, Resume> map = new LinkedHashMap<>();

                    while (rs.next()) {
                        String uuid = rs.getString("uuid");
                        Resume resume = map.get(uuid);
                        if (resume == null) {
                            resume = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                            map.put(uuid, resume);
                        }
                        addContactAndSection(rs, resume);
                    }
                    return new ArrayList<>(map.values());
                });
    }

//        public List<Resume> getAllSortedTwoQueries() {
//        return sqlHelper.transactionalExecute(connection -> {
//            Map<String, Resume> resumeMap = new LinkedHashMap<>();
//            try (PreparedStatement resumeSelector = connection.prepareStatement("" +
//                    "SELECT * " +
//                    "FROM resume " +
//                    "ORDER BY full_name, uuid");
//                 PreparedStatement contactSelector = connection.prepareStatement("" +
//                         "SELECT * " +
//                         "FROM contact")) {
//                ResultSet resultSetResumes = resumeSelector.executeQuery();
//                while (resultSetResumes.next()) {
//                    resumeMap.put(resultSetResumes.getString("uuid"), new Resume(resultSetResumes.getString("uuid"), resultSetResumes.getString("full_name")));
//                }
//
//                ResultSet resultSetContacts = contactSelector.executeQuery();
//                while (resultSetContacts.next()) {
//                    String uuid = resultSetContacts.getString("resume_uuid");
//                    addContact(resultSetContacts, resumeMap.get(uuid));
//                }
//            }
//            return new ArrayList<>(resumeMap.values());
//        });
//    }

    private void insertContactsAndSections(Resume resume, Connection connection) throws SQLException {
        try (PreparedStatement toInsertContacts = connection.prepareStatement("" +
                "INSERT INTO contact(value, resume_uuid, type) " +
                "VALUES(?, ?, ?)");
             PreparedStatement toInsertSections = connection.prepareStatement("" +
                     "INSERT INTO section(resume_uuid, type, value) " +
                     "VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> element : resume.getAllContacts().entrySet()) {
                toInsertContacts.setString(1, element.getValue());
                toInsertContacts.setString(2, resume.getUuid());
                toInsertContacts.setString(3, element.getKey().name());
                toInsertContacts.addBatch();
            }
            toInsertContacts.executeBatch();

            for (Map.Entry<SectionType, Section> entry : resume.getAllSections().entrySet()) {
                switch (entry.getKey()) {
                    case OBJECTIVE, PERSONAL -> {
                        toInsertSections.setString(1, resume.getUuid());
                        toInsertSections.setString(2, entry.getKey().name());
                        //
                        TextSection textSection = (TextSection) entry.getValue();
                        String textSectionDescription = textSection.getDescription();
                        //
                        toInsertSections.setString(3, textSectionDescription);
                        toInsertSections.addBatch();
                    }
                    case ACHIEVEMENTS, QUALIFICATIONS -> {
                        toInsertSections.setString(1, resume.getUuid());
                        toInsertSections.setString(2, entry.getKey().name());
                        //
                        ListSection listSection = (ListSection) entry.getValue();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String element : listSection.getData()) {
                            stringBuilder.append(element).append('\n');
                        }
                        //
                        toInsertSections.setString(3, stringBuilder.toString());
                        toInsertSections.addBatch();
                    }
                }
            }
            toInsertSections.executeBatch();
        }
    }

    private void deleteContactsAndSections(Resume resume) {
        sqlHelper.transactionalExecute(connection -> {
            try (PreparedStatement toDeleteContacts = connection.prepareStatement("" +
                    "DELETE FROM contact " +
                    "WHERE resume_uuid = ?");
                 PreparedStatement toDeleteSections = connection.prepareStatement("" +
                         "DELETE FROM section " +
                         "WHERE resume_uuid = ?")) {
                toDeleteContacts.setString(1, resume.getUuid());
                toDeleteSections.setString(1, resume.getUuid());
                toDeleteContacts.execute();
                toDeleteSections.execute();
            }
            return null;
        });
    }


    private void addContactAndSection(ResultSet rs, Resume resume) throws SQLException {
        String valueContact = rs.getString("value_contact");
        if (valueContact != null) {
            resume.addContact(ContactType.valueOf(rs.getString("type_contact")), valueContact);
        }

        String sectionTypeDb = rs.getString("type_section");
        if (sectionTypeDb != null) {
            SectionType sectionType = SectionType.valueOf(sectionTypeDb);
            String valueSection = rs.getString("value_section");
            switch (sectionType) {
                case PERSONAL, OBJECTIVE -> resume.getAllSections().put(sectionType, new TextSection(valueSection));
                case QUALIFICATIONS, ACHIEVEMENTS -> resume.getAllSections().put(sectionType, new ListSection(valueSection.split("\n")));
            }
        }
    }
}


