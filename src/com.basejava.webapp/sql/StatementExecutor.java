package com.basejava.webapp.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementExecutor {
    void doExecute(PreparedStatement ps) throws SQLException;
}
