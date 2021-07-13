package exception;

public class NotExistingStorageException extends StorageException {
    public NotExistingStorageException(String uuid) {
        super("ID " + uuid + " was not found.", uuid);
    }
}
