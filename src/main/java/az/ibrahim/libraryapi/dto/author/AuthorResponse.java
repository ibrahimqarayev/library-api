package az.ibrahim.libraryapi.dto.author;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorResponse {

    private Long id;
    private String name;
    private String email;
}