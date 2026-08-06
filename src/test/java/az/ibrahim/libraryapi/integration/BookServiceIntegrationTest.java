package az.ibrahim.libraryapi.integration;

import az.ibrahim.libraryapi.dto.book.CreateBookRequest;
import az.ibrahim.libraryapi.entity.Author;
import az.ibrahim.libraryapi.entity.Category;
import az.ibrahim.libraryapi.exception.CategoryNotFoundException;
import az.ibrahim.libraryapi.repository.AuthorRepository;
import az.ibrahim.libraryapi.repository.BookRepository;
import az.ibrahim.libraryapi.repository.CategoryRepository;
import az.ibrahim.libraryapi.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.AssertThrows.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldRollbackTransactionWhenCategoryNotFound() {

        // Arrange
        Author author = Author.builder()
                .name("Joshua Bloch")
                .email("joshua.bloch@example.com")
                .build();

        author = authorRepository.save(author);

        Category category = Category.builder()
                .name("Java")
                .build();

        category = categoryRepository.save(category);

        long initialBookCount = bookRepository.count();

        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Effective Java");
        request.setIsbn("9780134685991");
        request.setPublicationYear(2018);
        request.setAuthorId(author.getId());


        request.setCategoryIds(List.of(category.getId(), 999L));

        // Act & Assert
        assertThrows(
                CategoryNotFoundException.class,
                () -> bookService.create(request)
        );

        assertEquals(initialBookCount, bookRepository.count());
    }
}