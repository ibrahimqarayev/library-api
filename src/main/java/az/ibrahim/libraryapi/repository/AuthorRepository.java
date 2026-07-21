package az.ibrahim.libraryapi.repository;

import az.ibrahim.libraryapi.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author,Long> {
}
