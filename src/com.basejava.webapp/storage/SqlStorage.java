package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ExistingStorageException;
import com.basejava.webapp.exception.NotExistingStorageException;
import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.ConnectionFactory;
import com.basejava.webapp.util.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        SqlHelper.execute(connectionFactory, "DELETE FROM resume\n", (PreparedStatement::execute));
    }

    @Override
    public Resume get(String uuid) {
        AtomicReference<String> fullName = new AtomicReference<>();
        SqlHelper.execute(connectionFactory, "SELECT * FROM resume r WHERE r.uuid =?\n", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistingStorageException(uuid);
            }
            fullName.set(rs.getString("full_name"));
        });
        return new Resume(uuid, fullName.get());
    }

    @Override
    public void update(Resume resume) {
        String uuid = resume.getUuid();
        String fullName = resume.getFullName();
        SqlHelper.execute(connectionFactory, "UPDATE resume SET full_name = ? WHERE uuid = ?", ps -> {
            ps.setString(1, fullName);
            ps.setString(2, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistingStorageException(uuid);
            }
        });
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        try {
            SqlHelper.execute(connectionFactory, "INSERT INTO resume(uuid, full_name) VALUES (?, ?)\n", ps -> {
                ps.setString(1, uuid);
                ps.setString(2, resume.getFullName());
                ps.execute();
            });
        } catch (StorageException e) {
            throw new ExistingStorageException(uuid);
        }
    }

    @Override
    public void delete(String uuid) {
        SqlHelper.execute(connectionFactory, "DELETE FROM resume WHERE uuid=?\n", ps -> {
            ps.setString(1, uuid);
            if (!ps.execute()) {
                throw new NotExistingStorageException(uuid);
            }
        });
    }

    @Override
    public int size() {
        AtomicInteger size = new AtomicInteger();
        SqlHelper.execute(connectionFactory, "SELECT COUNT(r.uuid) FROM resume r;\n", ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new StorageException("Error while counting records in the database");
            }
            size.set(Integer.parseInt(rs.getString("count")));
        });
        return size.get();
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = new ArrayList<>();
        SqlHelper.execute(connectionFactory, "SELECT * FROM resume r ORDER BY r.full_name, r.uuid;\n", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Resume(getString(rs, "uuid"), getString(rs, "full_name")));
            }
        });
        return list;
    }

    private static String getString(ResultSet resultSet, String columnLabel) throws SQLException {
        return resultSet.getString(columnLabel).trim();
    }
}
