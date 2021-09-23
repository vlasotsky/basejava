package com.basejava.webapp.storage;

import com.basejava.webapp.exception.NotExistingStorageException;
import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.ConnectionFactory;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        ConnectionFactory connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        sqlHelper = new SqlHelper(connectionFactory);
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
                        if (rs.getString("type") != null) {
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
            try (PreparedStatement ps = connection.prepareStatement("" +
                    "UPDATE resume " +
                    "SET full_name = ? " +
                    "WHERE uuid = ?")) {
                insertUuidFullName(resume, ps);
            }

            try (PreparedStatement toGetOldRecords = connection.prepareStatement("" +
                    "SELECT * " +
                    "FROM contact c " +
                    "WHERE c.resume_uuid = ?");
                 PreparedStatement toDelete = connection.prepareStatement("" +
                         "DELETE FROM contact c " +
                         "WHERE c.resume_uuid = ? " +
                         "AND c.type = ?");
                 PreparedStatement toUpdate = connection.prepareStatement("" +
                         "UPDATE contact " +
                         "SET value = ? " +
                         "WHERE resume_uuid = ? AND type = ?");
                 PreparedStatement toDeleteAll = connection.prepareStatement("" +
                         "DELETE " +
                         "FROM contact " +
                         "WHERE resume_uuid = ?");
                 PreparedStatement toSaveContact = connection.prepareStatement("" +
                         "INSERT INTO contact(value, resume_uuid, type) " +
                         "VALUES(?, ?, ?) ")
            ) {
                //obtaining old records for this id
                String uuid = resume.getUuid();
                Map<ContactType, String> newContactsMap = resume.getAllContacts();
                toGetOldRecords.setString(1, uuid);
                ResultSet oldRecordsRs = toGetOldRecords.executeQuery();
                //running through old records to check if any of 'type' values exist in a map as keys
                //otherwise delete them from the database
                if (newContactsMap.isEmpty()) {
                    toDeleteAll.execute();
                } else if (!oldRecordsRs.isBeforeFirst()) {
                    insertAllContacts(resume, toSaveContact);
                } else {
                    while (oldRecordsRs.next()) {
                        String typeAsString = oldRecordsRs.getString("type");
                        ContactType typeFromOldRecords = ContactType.valueOf(typeAsString);
                        String newResumeValue = newContactsMap.get(typeFromOldRecords);

                        if (!newContactsMap.containsKey(typeFromOldRecords)) {
                            toDelete.setString(1, uuid);
                            toDelete.setString(2, typeAsString);
                            toDelete.addBatch();
                        } else if (newContactsMap.containsKey(typeFromOldRecords)
                                && !(newResumeValue.equals(oldRecordsRs.getString("value")))) {
                            //if there is such a record but the value differs -> update the value in the DB
                            toUpdate.setString(1, newResumeValue);
                            toUpdate.setString(2, uuid);
                            toUpdate.setString(3, typeAsString);
                            toUpdate.addBatch();
                        }
                    }
                    toUpdate.executeBatch();
                    toDelete.executeBatch();
                }
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
                        insertUuidFullName(resume, ps);
                    }
                    try (PreparedStatement ps = connection.prepareStatement("" +
                            "INSERT INTO contact(value, resume_uuid, type) " +
                            "VALUES(?, ?, ?)")) {
                        insertAllContacts(resume, ps);
                        return null;
                    }
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
                            "SELECT * " +
                            "FROM resume r " +
                            "ORDER BY r.full_name, r.uuid")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            Resume resume = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                            list.add(resume);
                        }
                    }
                    try (PreparedStatement ps = connection.prepareStatement("" +
                            "SELECT * " +
                            "FROM contact c " +
                            "WHERE c.resume_uuid = ?")) {
                        for (Resume element : list) {
                            ps.setString(1, element.getUuid());
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                                element.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                            }
                        }
                    }
                    return null;
                });
        return list;
    }

    private void insertUuidFullName(Resume resume, PreparedStatement ps) throws SQLException {
        ps.setString(1, resume.getFullName());
        ps.setString(2, resume.getUuid());
        if (ps.executeUpdate() == 0) {
            throw new NotExistingStorageException(resume.getUuid());
        }
    }

    private void insertAllContacts(Resume resume, PreparedStatement ps) throws SQLException {
        for (Map.Entry<ContactType, String> element : resume.getAllContacts().entrySet()) {
            ps.setString(1, element.getValue());
            ps.setString(2, resume.getUuid());
            ps.setString(3, element.getKey().name());
            ps.addBatch();
        }
        ps.executeBatch();
    }
}

