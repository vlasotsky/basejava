package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ExistingStorageException;
import com.basejava.webapp.exception.NotExistingStorageException;
import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM resume\n")) {
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public Resume get(String uuid) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM resume r WHERE r.uuid =?\n")) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistingStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void update(Resume resume) {
        String uuid = resume.getUuid();
        String fullName = resume.getFullName();
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
            ps.setString(1, fullName);
            ps.setString(2, uuid);
            if (ps.executeUpdate() == 0) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new NotExistingStorageException(uuid);
        }
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO resume(uuid, full_name) VALUES (?, ?)\n")) {
            ps.setString(1, uuid);
            ps.setString(2, resume.getFullName());
            ps.execute();
        } catch (SQLException e) {
            throw new ExistingStorageException(uuid);
        }
    }

    @Override
    public void delete(String uuid) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM resume WHERE uuid=?\n")) {
            ps.setString(1, uuid);
            if (!ps.execute()) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new NotExistingStorageException(uuid);
        }
    }

    @Override
    public int size() {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(r.uuid) FROM resume r;\n")) {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new StorageException("Error while counting records in database");
            }
            return Integer.parseInt(rs.getString("count"));
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = new ArrayList<>();

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM resume r ORDER BY r.full_name, r.uuid;\n")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
//                list.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name").trim()));
                list.add(new Resume(getString(rs, "uuid"), getString(rs, "full_name")));
            }
            return list;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private static String getString(ResultSet resultSet, String columnLabel) throws SQLException {
        return resultSet.getString(columnLabel).trim();
    }
}
