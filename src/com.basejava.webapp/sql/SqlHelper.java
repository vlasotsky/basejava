package com.basejava.webapp.sql;

import com.basejava.webapp.exception.ExistingStorageException;
import com.basejava.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> T execute(String statement, StatementExecutor<T> executor) {
        try (Connection connection = this.connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {
            return executor.doExecute(ps);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new ExistingStorageException("");
            }
            throw new StorageException(e);
        }
    }
}
