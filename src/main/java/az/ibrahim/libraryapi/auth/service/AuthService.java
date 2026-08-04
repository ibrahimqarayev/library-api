package az.ibrahim.libraryapi.auth.service;

import az.ibrahim.libraryapi.auth.dto.request.LoginRequest;
import az.ibrahim.libraryapi.auth.dto.request.RegisterRequest;
import az.ibrahim.libraryapi.auth.dto.response.AuthResponse;
import az.ibrahim.libraryapi.entity.User;
import az.ibrahim.libraryapi.enums.Role;
import az.ibrahim.libraryapi.exception.EmailAlreadyExistsException;
import az.ibrahim.libraryapi.exception.UserNotFoundException;
import az.ibrahim.libraryapi.exception.UsernameAlreadyExistsException;
import az.ibrahim.libraryapi.repository.UserRepository;
import az.ibrahim.libraryapi.security.jwt.JwtService;
import az.ibrahim.libraryapi.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException("Username already exists.");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(new CustomUserDetails(savedUser));

        return new AuthResponse(
                token,
                savedUser.getUsername(),
                savedUser.getRole()
        );
    }

    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, userDetails.getUser().getUsername(), userDetails.getRole());
    }

}