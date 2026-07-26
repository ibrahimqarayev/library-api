package az.ibrahim.libraryapi.service;

import az.ibrahim.libraryapi.dto.book.BookResponse;
import az.ibrahim.libraryapi.dto.book.CreateBookRequest;
import az.ibrahim.libraryapi.dto.book.UpdateBookRequest;
import az.ibrahim.libraryapi.dto.pagination.PageResponse;
import az.ibrahim.libraryapi.entity.Author;
import az.ibrahim.libraryapi.entity.Book;
import az.ibrahim.libraryapi.exception.AuthorNotFoundException;
import az.ibrahim.libraryapi.exception.BookNotFoundException;
import az.ibrahim.libraryapi.mapper.BookMapper;
import az.ibrahim.libraryapi.mapper.PageMapper;
import az.ibrahim.libraryapi.repository.AuthorRepository;
import az.ibrahim.libraryapi.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final PageMapper pageMapper;

    @Transactional
    public BookResponse create(CreateBookRequest request) {
        Book book = bookMapper.toEntity(request);

        Author author = findAuthorById(request.getAuthorId());
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponse(savedBook);
    }

    public PageResponse<BookResponse> getAll(
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Book> bookPage = bookRepository.findAll(pageable);
        return pageMapper.toPageResponse(bookPage, bookMapper::toResponse);
    }

    public BookResponse getById(Long id) {
        Book book = findBookById(id);
        return bookMapper.toResponse(book);
    }

    @Transactional
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

    @Transactional
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
