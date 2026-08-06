package az.ibrahim.libraryapi.mapper;

import az.ibrahim.libraryapi.dto.category.CategoryResponse;
import az.ibrahim.libraryapi.dto.category.CreateCategoryRequest;
import az.ibrahim.libraryapi.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    Category toEntity(CreateCategoryRequest request);

    CategoryResponse toResponse(Category category);
}