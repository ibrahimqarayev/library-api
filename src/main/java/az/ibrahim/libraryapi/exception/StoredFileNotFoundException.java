package az.ibrahim.libraryapi.exception;

import org.springframework.http.HttpStatus;

public class StoredFileNotFoundException extends BusinessException {

    public StoredFileNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}