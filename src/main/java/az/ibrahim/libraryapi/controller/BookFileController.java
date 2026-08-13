package az.ibrahim.libraryapi.controller;

import az.ibrahim.libraryapi.entity.BookFile;
import az.ibrahim.libraryapi.service.BookFileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookFileController {

    private final BookFileService bookFileService;

    @PostMapping(value = "/{bookId}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload book file")
    public ResponseEntity<Void> upload(
            @PathVariable Long bookId,
            @RequestPart("file") MultipartFile file) {

        bookFileService.upload(bookId, file);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{bookId}/file")
    @Operation(summary = "Download book file")
    public ResponseEntity<Resource> download(@PathVariable Long bookId) {

        BookFile bookFile = bookFileService.getByBookId(bookId);

        Resource resource = bookFileService.download(bookId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(bookFile.getContentType()))
                .contentLength(bookFile.getFileSize())
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                bookFile.getFileName() + "\""
                )
                .body(resource);
    }

    @DeleteMapping("/{bookId}/file")
    @Operation(summary = "Delete book file")
    public ResponseEntity<Void> delete(@PathVariable Long bookId) {
        bookFileService.delete(bookId);
        return ResponseEntity.noContent().build();
    }
}