package az.ibrahim.libraryapi.dto.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponse {

    private Long id;
    private String name;
    private String email;
}