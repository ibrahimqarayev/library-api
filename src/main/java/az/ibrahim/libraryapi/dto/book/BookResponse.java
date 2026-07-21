package az.ibrahim.libraryapi.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

    private Long id;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private Long authorId;
}