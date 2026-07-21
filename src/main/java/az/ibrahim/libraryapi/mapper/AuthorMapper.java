package az.ibrahim.libraryapi.mapper;

import az.ibrahim.libraryapi.dto.author.AuthorResponse;
import az.ibrahim.libraryapi.dto.author.CreateAuthorRequest;
import az.ibrahim.libraryapi.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AuthorMapper {

    Author toEntity(CreateAuthorRequest request);

    AuthorResponse toResponse(Author author);
}