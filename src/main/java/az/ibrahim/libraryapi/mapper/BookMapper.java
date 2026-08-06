package az.ibrahim.libraryapi.mapper;

import az.ibrahim.libraryapi.dto.book.BookResponse;
import az.ibrahim.libraryapi.dto.book.CreateBookRequest;
import az.ibrahim.libraryapi.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", uses = CategoryMapper.class, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Book toEntity(CreateBookRequest request);

    @Mapping(target = "authorId", source = "author.id")
    BookResponse toResponse(Book book);
}