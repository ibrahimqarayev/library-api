package az.ibrahim.libraryapi.notification.event;

public record BookCreatedEvent(
        String email,
        String bookTitle
) {
}