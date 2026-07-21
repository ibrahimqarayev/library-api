package az.ibrahim.libraryapi.exception;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends BusinessException {
    public BookNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
