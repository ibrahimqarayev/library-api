package az.ibrahim.libraryapi.repository;

import az.ibrahim.libraryapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
