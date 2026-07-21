package az.ibrahim.libraryapi.mapper;

import az.ibrahim.libraryapi.dto.pagination.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PageMapper {

    public <T, R> PageResponse<R> toPageResponse(
            Page<T> page,
            Function<T, R> mapper
    ) {
        return new PageResponse<>(
                page.getContent()
                        .stream()
                        .map(mapper)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}