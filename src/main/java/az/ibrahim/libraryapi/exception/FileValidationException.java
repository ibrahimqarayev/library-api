package az.ibrahim.libraryapi.exception;

import org.springframework.http.HttpStatus;

public class FileValidationException extends BusinessException {

    public FileValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}