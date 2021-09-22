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
                        ContactType contactType = ContactType.valueOf(rs.getString("type"));
                        resume.addContact(contactType, value);
                    } while (rs.next());
                    return resume;
                });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.execute("UPDATE resume SET full_name = ? WHERE uuid = ?",
                ps -> {
                    doSetString(resume, ps);
                    return null;
                });

        sqlHelper.execute("" +
                        "UPDATE contact  " +
                        "SET value = ? " +
                        "WHERE resume_uuid = ? AND type = ?",
                ps -> {
                    doSetString(resume, ps);
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
                        doSetString(resume, ps);
                    }

                    try (PreparedStatement ps = connection.prepareStatement("" +
                            "INSERT INTO contact(value, resume_uuid, type) " +
                            "VALUES(?, ?, ?)")) {
                        doSetString(resume, ps);
                        return null;
                    }
                });
    }


    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid=?", ps -> {
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
        List<Resume> sortedList = sqlHelper.execute("" +
                        "SELECT * " +
                        "FROM resume r " +
                        "ORDER BY r.full_name, r.uuid",
                ps -> {
                    List<Resume> list = new ArrayList<>();
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Resume resume = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                        list.add(resume);
                    }
                    return list;
                });
        for (Resume element : sortedList) {
            sqlHelper.execute("" +
                            "SELECT * " +
                            "FROM contact c " +
                            "WHERE c.resume_uuid = ?",
                    ps -> {
                        ps.setString(1, element.getUuid());
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            element.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                        }
                        return null;
                    });
        }
        return sortedList;
    }

    private void doSetString(Resume resume, PreparedStatement ps) throws SQLException {
        if (ps.getParameterMetaData().getParameterCount() == 2) {
            ps.setString(1, resume.getFullName());
            ps.setString(2, resume.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistingStorageException(resume.getUuid());
            }
        } else {
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
