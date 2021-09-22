package com.basejava.webapp.sql;

import com.basejava.webapp.exception.ExistingStorageException;
import com.basejava.webapp.exception.StorageException;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

public class ExceptionUtil {
    private ExceptionUtil() {

    }

    public static StorageException convertException(SQLException sqlException) {
        if (sqlException instanceof PSQLException) {
            if (sqlException.getSQLState().equals("23505")) {
                return new ExistingStorageException(null);
            }
        }
        return new StorageException(sqlException);
    }
}
