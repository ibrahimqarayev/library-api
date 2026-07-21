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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final PageMapper pageMapper;

    public AuthorResponse create(CreateAuthorRequest request) {
        Author author = authorMapper.toEntity(request);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toResponse(savedAuthor);
    }

    public PageResponse<AuthorResponse> getAll(
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Author> authorPage = authorRepository.findAll(pageable);
        return pageMapper.toPageResponse(authorPage, authorMapper::toResponse);
    }

    public AuthorResponse getById(Long id) {
        Author author = findAuthorById(id);
        return authorMapper.toResponse(author);
    }

    public AuthorResponse update(Long id, UpdateAuthorRequest request) {
        Author author = findAuthorById(id);

        author.setName(request.getName());
        author.setEmail(request.getEmail());

        Author updateAuthor = authorRepository.save(author);
        return authorMapper.toResponse(updateAuthor);
    }

    public void delete(Long id) {
        Author author = findAuthorById(id);
        authorRepository.delete(author);
    }

    private Author findAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + id));
    }
}
