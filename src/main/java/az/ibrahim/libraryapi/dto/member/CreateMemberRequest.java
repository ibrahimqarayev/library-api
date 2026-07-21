package az.ibrahim.libraryapi.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CreateMemberRequest {

    private String name;
    private String email;
    private LocalDate membershipDate;
}