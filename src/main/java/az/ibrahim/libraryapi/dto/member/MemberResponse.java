package az.ibrahim.libraryapi.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String name;
    private String email;
    private LocalDate membershipDate;
}