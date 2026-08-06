package az.ibrahim.libraryapi.specification;

import az.ibrahim.libraryapi.entity.Author;
import az.ibrahim.libraryapi.entity.Book;
import az.ibrahim.libraryapi.entity.Category;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class BookSpecification {

    private BookSpecification() {
    }

    public static Specification<Book> filter(
            String title,
            String author,
            String category,
            Integer publicationYear
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + title.toLowerCase() + "%"
                        )
                );
            }

            if (author != null && !author.isBlank()) {

                Join<Book, Author> authorJoin = root.join("author");

                predicates.add(
                        cb.like(
                                cb.lower(authorJoin.get("name")),
                                "%" + author.toLowerCase() + "%"
                        )
                );
            }

            if (category != null && !category.isBlank()) {

                Join<Book, Category> categoryJoin = root.join("categories");

                predicates.add(
                        cb.equal(
                                cb.lower(categoryJoin.get("name")),
                                category.toLowerCase()
                        )
                );
            }

            if (publicationYear != null) {

                predicates.add(
                        cb.equal(root.get("publicationYear"), publicationYear)
                );
            }

            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}