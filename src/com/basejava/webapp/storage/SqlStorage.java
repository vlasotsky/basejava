package com.basejava.webapp.storage;

import com.basejava.webapp.exception.NotExistingStorageException;
import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.model.Section;
import com.basejava.webapp.model.SectionType;
import com.basejava.webapp.sql.SqlHelper;
import com.basejava.webapp.util.JsonParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
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
            deleteContacts(resume, connection);
            deleteSections(resume, connection);
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
            if (ps.executeUpdate() == 0) {
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

    private void insertContactsAndSections(Resume resume, Connection connection) throws SQLException {
        try (PreparedStatement toInsertContacts = connection.prepareStatement("" +
                "INSERT INTO contact(value, resume_uuid, type) " +
                "VALUES(?, ?, ?)")
        ) {
            for (Map.Entry<ContactType, String> element : resume.getContacts().entrySet()) {
                toInsertContacts.setString(1, element.getValue());
                toInsertContacts.setString(2, resume.getUuid());
                toInsertContacts.setString(3, element.getKey().name());
                toInsertContacts.addBatch();
            }
            toInsertContacts.executeBatch();
        }
        try (PreparedStatement toInsertSections = connection.prepareStatement("" +
                "INSERT INTO section(resume_uuid, type, value) " +
                "VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> entry : resume.getSections().entrySet()) {

                toInsertSections.setString(1, resume.getUuid());
                toInsertSections.setString(2, entry.getKey().name());
                Section section = entry.getValue();
                toInsertSections.setString(3, JsonParser.write(section, Section.class));
                toInsertSections.addBatch();
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

    private void deleteContacts(Resume resume, Connection connection) throws SQLException {
        deleteAttributes(connection, "" +
                "DELETE FROM contact " +
                "WHERE resume_uuid = ?", resume);
    }

    private void deleteSections(Resume resume, Connection connection) throws SQLException {
        deleteAttributes(connection, "" +
                "DELETE FROM section " +
                "WHERE resume_uuid = ?", resume);
    }

    private void deleteAttributes(Connection connection, String sql, Resume resume) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, resume.getUuid());
            ps.execute();
        }
    }


    private void addContactAndSection(ResultSet rs, Resume resume) throws SQLException {
        String valueContact = rs.getString("value_contact");
        if (valueContact != null) {
            resume.addContact(ContactType.valueOf(rs.getString("type_contact")), valueContact);
        }

        String content = rs.getString("type_section");
        if (content != null) {
            SectionType sectionType = SectionType.valueOf(content);
            String valueSection = rs.getString("value_section");
            resume.addSection(sectionType, JsonParser.read(valueSection, Section.class));
        }
    }
}


