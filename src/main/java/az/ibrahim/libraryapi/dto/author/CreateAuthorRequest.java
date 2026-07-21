package az.ibrahim.libraryapi.dto.author;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateAuthorRequest {

    private String name;
    private String email;
}
