package az.ibrahim.libraryapi.service;

import az.ibrahim.libraryapi.dto.author.AuthorResponse;
import az.ibrahim.libraryapi.dto.author.CreateAuthorRequest;
import az.ibrahim.libraryapi.dto.author.UpdateAuthorRequest;
import az.ibrahim.libraryapi.entity.Author;
import az.ibrahim.libraryapi.exception.AuthorNotFoundException;
import az.ibrahim.libraryapi.mapper.AuthorMapper;
import az.ibrahim.libraryapi.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorResponse create(CreateAuthorRequest request) {
        Author author = authorMapper.toEntity(request);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toResponse(savedAuthor);
    }

    public List<AuthorResponse> getAll() {
        return authorRepository.findAll()
                .stream().map(authorMapper::toResponse)
                .collect(Collectors.toList());
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
