package az.ibrahim.libraryapi.repository;

import az.ibrahim.libraryapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
}
