package com.basejava.webapp.util;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.sql.ConnectionFactory;
import com.basejava.webapp.sql.StatementExecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    public static void execute(ConnectionFactory connectionFactory, String statement, StatementExecutor executor) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {
            executor.doExecute(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
