package az.ibrahim.libraryapi.service;

import az.ibrahim.libraryapi.dto.book.BookResponse;
import az.ibrahim.libraryapi.dto.book.CreateBookRequest;
import az.ibrahim.libraryapi.dto.book.UpdateBookRequest;
import az.ibrahim.libraryapi.entity.Author;
import az.ibrahim.libraryapi.entity.Book;
import az.ibrahim.libraryapi.exception.AuthorNotFoundException;
import az.ibrahim.libraryapi.exception.BookNotFoundException;
import az.ibrahim.libraryapi.mapper.BookMapper;
import az.ibrahim.libraryapi.repository.AuthorRepository;
import az.ibrahim.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public BookResponse create(CreateBookRequest request) {
        Book book = bookMapper.toEntity(request);

        Author author = findAuthorById(request.getAuthorId());
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponse(savedBook);
    }

    public List<BookResponse> getAll() {
        return bookRepository.findAll()
                .stream().map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    public BookResponse getById(Long id) {
        Book book = findBookById(id);
        return bookMapper.toResponse(book);
    }

    public BookResponse update(Long id, UpdateBookRequest request) {
        Book book = findBookById(id);

        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setPublicationYear(request.getPublicationYear());

        Author author = findAuthorById(request.getAuthorId());
        book.setAuthor(author);

        Book updatedBook = bookRepository.save(book);
        return bookMapper.toResponse(updatedBook);
    }

    public void delete(Long id) {
        Book book = findBookById(id);
        bookRepository.delete(book);
    }

    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    }

    private Author findAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + id));
    }
}
