package az.ibrahim.libraryapi.notification;

public interface NotificationService {

    void sendBookCreatedNotification(String email, String bookTitle);
}