package az.ibrahim.libraryapi.auth.dto.response;

import az.ibrahim.libraryapi.enums.Role;

public record AuthResponse(

        String token,
        String username,
        Role role
) {
}