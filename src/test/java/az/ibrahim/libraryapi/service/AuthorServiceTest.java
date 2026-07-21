package az.ibrahim.libraryapi.service;

import az.ibrahim.libraryapi.dto.author.AuthorResponse;
import az.ibrahim.libraryapi.dto.author.CreateAuthorRequest;
import az.ibrahim.libraryapi.dto.author.UpdateAuthorRequest;
import az.ibrahim.libraryapi.dto.pagination.PageResponse;
import az.ibrahim.libraryapi.entity.Author;
import az.ibrahim.libraryapi.exception.AuthorNotFoundException;
import az.ibrahim.libraryapi.mapper.AuthorMapper;
import az.ibrahim.libraryapi.mapper.PageMapper;
import az.ibrahim.libraryapi.repository.AuthorRepository;
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
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private PageMapper pageMapper;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void shouldCreateAuthor() {
        CreateAuthorRequest request = new CreateAuthorRequest();
        Author author = new Author();
        Author savedAuthor = new Author();
        AuthorResponse response = new AuthorResponse();

        when(authorMapper.toEntity(request)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(savedAuthor);
        when(authorMapper.toResponse(savedAuthor)).thenReturn(response);

        AuthorResponse result = authorService.create(request);

        assertEquals(response, result);

        verify(authorMapper).toEntity(request);
        verify(authorRepository).save(author);
        verify(authorMapper).toResponse(savedAuthor);
    }

    @Test
    void shouldReturnPagedAuthors() {
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String sortDirection = "asc";

        Page<Author> authorPage = mock(Page.class);

        PageResponse<AuthorResponse> pageResponse = new PageResponse<>(List.of(), page, size, 0L, 0, true);

        when(authorRepository.findAll(any(Pageable.class))).thenReturn(authorPage);

        when(pageMapper.toPageResponse(eq(authorPage), ArgumentMatchers.<Function<Author, AuthorResponse>>any()))
                .thenReturn(pageResponse);

        PageResponse<AuthorResponse> result = authorService.getAll(page, size, sortBy, sortDirection);

        assertEquals(pageResponse, result);

        verify(authorRepository).findAll(any(Pageable.class));

        verify(pageMapper).toPageResponse(eq(authorPage), ArgumentMatchers.<Function<Author, AuthorResponse>>any());
    }

    @Test
    void shouldReturnAuthorById() {
        Long authorId = 1L;

        Author author = new Author();
        AuthorResponse response = new AuthorResponse();

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        when(authorMapper.toResponse(author)).thenReturn(response);

        AuthorResponse result = authorService.getById(authorId);

        assertEquals(response, result);

        verify(authorRepository).findById(authorId);
        verify(authorMapper).toResponse(author);
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFound() {
        Long authorId = 1L;

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorService.getById(authorId));

        verify(authorRepository).findById(authorId);
        verify(authorMapper, never()).toResponse(any());
    }

    @Test
    void shouldUpdateAuthor() {
        Long authorId = 1L;

        UpdateAuthorRequest request = new UpdateAuthorRequest();
        request.setName("Updated Author");
        request.setEmail("updated@email.com");

        Author author = new Author();
        AuthorResponse response = new AuthorResponse();

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        when(authorRepository.save(author)).thenReturn(author);

        when(authorMapper.toResponse(author)).thenReturn(response);

        AuthorResponse result = authorService.update(authorId, request);

        assertEquals(response, result);
        assertEquals(request.getName(), author.getName());
        assertEquals(request.getEmail(), author.getEmail());

        verify(authorRepository).findById(authorId);
        verify(authorRepository).save(author);
        verify(authorMapper).toResponse(author);
    }

    @Test
    void shouldDeleteAuthor() {
        Long authorId = 1L;

        Author author = new Author();

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        authorService.delete(authorId);

        verify(authorRepository).findById(authorId);
        verify(authorRepository).delete(author);
    }
}