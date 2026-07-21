package az.ibrahim.libraryapi.exception;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
