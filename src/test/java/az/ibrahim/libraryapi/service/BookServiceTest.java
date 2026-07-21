package az.ibrahim.libraryapi.service;

import az.ibrahim.libraryapi.dto.book.BookResponse;
import az.ibrahim.libraryapi.dto.book.CreateBookRequest;
import az.ibrahim.libraryapi.dto.book.UpdateBookRequest;
import az.ibrahim.libraryapi.dto.pagination.PageResponse;
import az.ibrahim.libraryapi.entity.Author;
import az.ibrahim.libraryapi.entity.Book;
import az.ibrahim.libraryapi.exception.BookNotFoundException;
import az.ibrahim.libraryapi.mapper.BookMapper;
import az.ibrahim.libraryapi.mapper.PageMapper;
import az.ibrahim.libraryapi.repository.AuthorRepository;
import az.ibrahim.libraryapi.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private PageMapper pageMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldCreateBook() {
        Long authorId = 1L;

        CreateBookRequest request = new CreateBookRequest();
        request.setAuthorId(authorId);

        Book book = new Book();
        Book savedBook = new Book();
        Author author = new Author();
        BookResponse response = new BookResponse();

        when(bookMapper.toEntity(request)).thenReturn(book);

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        when(bookRepository.save(book)).thenReturn(savedBook);

        when(bookMapper.toResponse(savedBook)).thenReturn(response);

        BookResponse result = bookService.create(request);

        assertEquals(response, result);
        assertEquals(author, book.getAuthor());

        verify(bookMapper).toEntity(request);
        verify(authorRepository).findById(authorId);
        verify(bookRepository).save(book);
        verify(bookMapper).toResponse(savedBook);
    }

    @Test
    void shouldReturnPagedBooks() {
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String sortDirection = "asc";

        Page<Book> bookPage = mock(Page.class);

        PageResponse<BookResponse> pageResponse = new PageResponse<>(List.of(), page, size, 0L, 0, true
        );

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        when(pageMapper.toPageResponse(eq(bookPage), ArgumentMatchers.<Function<Book, BookResponse>>any()))
                .thenReturn(pageResponse);

        PageResponse<BookResponse> result = bookService.getAll(page, size, sortBy, sortDirection);

        assertEquals(pageResponse, result);

        verify(bookRepository).findAll(any(Pageable.class));

        verify(pageMapper).toPageResponse(eq(bookPage), ArgumentMatchers.<Function<Book, BookResponse>>any());
    }

    @Test
    void shouldReturnBookById() {
        Long bookId = 1L;

        Book book = new Book();
        BookResponse response = new BookResponse();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        when(bookMapper.toResponse(book)).thenReturn(response);

        BookResponse result = bookService.getById(bookId);

        assertEquals(response, result);

        verify(bookRepository).findById(bookId);
        verify(bookMapper).toResponse(book);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getById(bookId));

        verify(bookRepository).findById(bookId);
        verify(bookMapper, never()).toResponse(any());
    }

    @Test
    void shouldUpdateBook() {
        Long bookId = 1L;
        Long authorId = 2L;

        UpdateBookRequest request = new UpdateBookRequest();
        request.setTitle("Updated Book");
        request.setIsbn("978-1234567890");
        request.setPublicationYear(2025);
        request.setAuthorId(authorId);

        Book book = new Book();
        Author author = new Author();
        BookResponse response = new BookResponse();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        when(bookRepository.save(book)).thenReturn(book);

        when(bookMapper.toResponse(book)).thenReturn(response);

        BookResponse result = bookService.update(bookId, request);

        assertEquals(response, result);

        assertEquals(request.getTitle(), book.getTitle());
        assertEquals(request.getIsbn(), book.getIsbn());
        assertEquals(request.getPublicationYear(), book.getPublicationYear());
        assertEquals(author, book.getAuthor());

        verify(bookRepository).findById(bookId);
        verify(authorRepository).findById(authorId);
        verify(bookRepository).save(book);
        verify(bookMapper).toResponse(book);
    }

    @Test
    void shouldDeleteBook() {
        Long bookId = 1L;

        Book book = new Book();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        bookService.delete(bookId);

        verify(bookRepository).findById(bookId);
        verify(bookRepository).delete(book);
    }
}