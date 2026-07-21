package az.ibrahim.libraryapi.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String name;
    private String email;
    private LocalDate membershipDate;
}