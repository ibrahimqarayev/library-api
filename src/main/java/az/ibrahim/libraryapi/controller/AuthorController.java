package az.ibrahim.libraryapi.controller;

import az.ibrahim.libraryapi.dto.author.AuthorResponse;
import az.ibrahim.libraryapi.dto.author.CreateAuthorRequest;
import az.ibrahim.libraryapi.dto.author.UpdateAuthorRequest;
import az.ibrahim.libraryapi.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorResponse> create(@RequestBody CreateAuthorRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authorService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAll(){
        return ResponseEntity.ok(authorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(authorService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> update(@PathVariable Long id, @RequestBody UpdateAuthorRequest request){
        return ResponseEntity.ok(authorService.update(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
