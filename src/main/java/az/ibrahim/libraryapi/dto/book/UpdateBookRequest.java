package az.ibrahim.libraryapi.dto.book;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateBookRequest {

    private String title;
    private String isbn;
    private Integer publicationYear;
    private Long authorId;
}