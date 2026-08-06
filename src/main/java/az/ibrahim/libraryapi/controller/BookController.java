package az.ibrahim.libraryapi.controller;

import az.ibrahim.libraryapi.dto.book.BookResponse;
import az.ibrahim.libraryapi.dto.book.CreateBookRequest;
import az.ibrahim.libraryapi.dto.book.UpdateBookRequest;
import az.ibrahim.libraryapi.dto.pagination.PageResponse;
import az.ibrahim.libraryapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
@Tag(name = "Book", description = "Book management APIs")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Create a new book")
    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody CreateBookRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.create(request));
    }

    @Operation(summary = "Get all books with pagination and sorting")
    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        return ResponseEntity.ok(bookService.getAll(page, size, sortBy, sortDirection));
    }

    @Operation(summary = "Get a book by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @Operation(summary = "Update a book")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateBookRequest request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    @Operation(summary = "Delete a book")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search books",
            description = "Searches books dynamically using optional filters such as title, author, category, and publication year."
    )
    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer publicationYear
    ) {

        return ResponseEntity.ok(bookService.search(title, author, category, publicationYear));
    }

}