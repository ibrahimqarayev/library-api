package az.ibrahim.libraryapi.notification.event;

import az.ibrahim.libraryapi.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookCreatedEventListener {

    private final NotificationService notificationService;

    @Async("taskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(BookCreatedEvent event) {
        notificationService.sendBookCreatedNotification(event.email(), event.bookTitle());
    }
}
