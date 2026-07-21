package az.ibrahim.libraryapi.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CreateMemberRequest {

    @NotBlank(message = "Member name is required")
    @Size(min = 2, max = 100, message = "Member name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Member email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Membership date is required")
    private LocalDate membershipDate;
}