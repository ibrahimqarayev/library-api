package az.ibrahim.libraryapi.exception;

import org.springframework.http.HttpStatus;

public class AuthorNotFoundException extends BusinessException {
    public AuthorNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
