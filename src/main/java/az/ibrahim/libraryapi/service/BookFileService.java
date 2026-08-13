package az.ibrahim.libraryapi.service;

import az.ibrahim.libraryapi.entity.Book;
import az.ibrahim.libraryapi.entity.BookFile;
import az.ibrahim.libraryapi.exception.BookNotFoundException;
import az.ibrahim.libraryapi.exception.StoredFileNotFoundException;
import az.ibrahim.libraryapi.repository.BookFileRepository;
import az.ibrahim.libraryapi.repository.BookRepository;
import az.ibrahim.libraryapi.service.inter.FileStorageService;
import az.ibrahim.libraryapi.validator.FileValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookFileService {

    private final BookRepository bookRepository;
    private final BookFileRepository bookFileRepository;
    private final FileStorageService fileStorageService;
    private final FileValidator fileValidator;

    @Transactional
    public void upload(Long bookId, MultipartFile file) {

        fileValidator.validate(file);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        String extension = fileValidator.getExtension(file.getOriginalFilename());

        String storedFileName = UUID.randomUUID() + extension;

        String filePath = fileStorageService.store(
                file,
                storedFileName
        );

        Optional<BookFile> existingFile = bookFileRepository.findByBookId(bookId);

        if (existingFile.isPresent()) {

            BookFile bookFile = existingFile.get();

            fileStorageService.delete(bookFile.getFilePath());

            bookFile.setFileName(file.getOriginalFilename());
            bookFile.setFilePath(filePath);
            bookFile.setContentType(file.getContentType());
            bookFile.setFileSize(file.getSize());

            bookFileRepository.save(bookFile);

        } else {

            BookFile bookFile = BookFile.builder()
                    .fileName(file.getOriginalFilename())
                    .filePath(filePath)
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .book(book)
                    .build();

            bookFileRepository.save(bookFile);
        }
    }

    @Transactional(readOnly = true)
    public BookFile getByBookId(Long bookId) {

        return bookFileRepository.findByBookId(bookId)
                .orElseThrow(() -> new StoredFileNotFoundException("File not found for book with id: " + bookId));
    }

    @Transactional(readOnly = true)
    public Resource download(Long bookId) {

        BookFile bookFile = getByBookId(bookId);

        return fileStorageService.load(bookFile.getFilePath());
    }

    @Transactional
    public void delete(Long bookId) {

        BookFile bookFile = getByBookId(bookId);

        fileStorageService.delete(bookFile.getFilePath());

        bookFileRepository.delete(bookFile);
    }
}