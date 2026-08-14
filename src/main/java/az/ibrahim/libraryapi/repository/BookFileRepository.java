package az.ibrahim.libraryapi.repository;

import az.ibrahim.libraryapi.entity.BookFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookFileRepository extends JpaRepository<BookFile, Long> {

    Optional<BookFile> findByBookId(Long bookId);

    @Query("select b.filePath from BookFile b")
    List<String> findAllFilePaths();
}