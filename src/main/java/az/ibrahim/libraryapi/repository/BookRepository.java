package az.ibrahim.libraryapi.repository;

import az.ibrahim.libraryapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByPublicationYearBetween(Integer startYear, Integer endYear);

    @Query("""
            SELECT b
            FROM Book b
            JOIN b.author a
            WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :authorName, '%'))
            """)
    List<Book> findByAuthorName(@Param("authorName") String authorName);

    @Query("""
            SELECT b
            FROM Book b
            JOIN b.categories c
            WHERE LOWER(c.name) = LOWER(:categoryName)
            """)
    List<Book> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("""
            SELECT DISTINCT b
            FROM Book b
            JOIN b.author a
            JOIN b.categories c
            WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :authorName, '%'))
              AND LOWER(c.name) = LOWER(:categoryName)
            """)
    List<Book> findByAuthorAndCategory(
            @Param("authorName") String authorName,
            @Param("categoryName") String categoryName
    );
}
