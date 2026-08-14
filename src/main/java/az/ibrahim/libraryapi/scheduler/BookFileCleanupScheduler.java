package az.ibrahim.libraryapi.scheduler;

import az.ibrahim.libraryapi.repository.BookFileRepository;
import az.ibrahim.libraryapi.service.inter.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BookFileCleanupScheduler {

    private final BookFileRepository bookFileRepository;
    private final FileStorageService fileStorageService;

    @Scheduled(cron = "${file.cleanup.cron}")
    public void cleanup() {

        Set<String> storedFilePaths = new HashSet<>(bookFileRepository.findAllFilePaths());

        List<String> files = fileStorageService.listFiles();

        files.stream()
                .filter(filePath -> !storedFilePaths.contains(filePath))
                .forEach(fileStorageService::delete);
    }
}