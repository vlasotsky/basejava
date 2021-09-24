package com.basejava.webapp.storage;

import com.basejava.webapp.exception.NotExistingStorageException;
import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", (PreparedStatement::execute));
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                        "SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "ON r.uuid = c.resume_uuid " +
                        "WHERE r.uuid =? ",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistingStorageException(uuid);
                    }
                    Resume resume = new Resume(uuid, rs.getString("full_name"));
                    do {
                        String value = rs.getString("value");
                        if (value != null) {
                            ContactType contactType = ContactType.valueOf(rs.getString("type"));
                            resume.addContact(contactType, value);
                        }
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

            try (PreparedStatement toDeleteAll = connection.prepareStatement("" +
                    "DELETE FROM contact " +
                    "WHERE resume_uuid = ?")) {
                toDeleteAll.setString(1, uuid);
                toDeleteAll.execute();
                insertAllContacts(resume, connection);
            }
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
                    }
                    insertAllContacts(resume, connection);
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
        return sqlHelper.execute("SELECT COUNT(r.uuid) FROM resume r", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("count");
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = new ArrayList<>();
        sqlHelper.transactionalExecute(
                connection -> {
                    try (PreparedStatement ps = connection.prepareStatement("" +
                            "SELECT r.uuid, r.full_name, c.type, c.value " +
                            "FROM resume r " +
                            "LEFT JOIN contact c ON (r.uuid = c.resume_uuid) " +
                            "ORDER BY r.full_name, r.uuid")) {
                        ResultSet rs = ps.executeQuery();
                        rs.next();
                        Resume resume = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                        do {
                            String resumeUuid = resume.getUuid();
                            String uuidDb = rs.getString("uuid");
                            String value = rs.getString("value");

                            if (!uuidDb.equals(resumeUuid)) {
                                list.add(resume);
                                resume = new Resume(uuidDb, rs.getString("full_name"));
                            }
                            if (value != null) {
                                resume.addContact(ContactType.valueOf(rs.getString("type")), value);
                            }
                        } while (rs.next());
                        list.add(resume);
                    }
                    return null;
                });
        return list;
    }

    private void insertAllContacts(Resume resume, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("" +
                "INSERT INTO contact(value, resume_uuid, type) " +
                "VALUES(?, ?, ?)")) {
            for (Map.Entry<ContactType, String> element : resume.getAllContacts().entrySet()) {
                ps.setString(1, element.getValue());
                ps.setString(2, resume.getUuid());
                ps.setString(3, element.getKey().name());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}

