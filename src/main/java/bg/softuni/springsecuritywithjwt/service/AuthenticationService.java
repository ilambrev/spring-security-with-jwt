package bg.softuni.springsecuritywithjwt.service;

import bg.softuni.springsecuritywithjwt.model.dto.AuthenticationRequest;
import bg.softuni.springsecuritywithjwt.model.dto.AuthenticationResponse;
import bg.softuni.springsecuritywithjwt.model.dto.RegisterRequest;
import bg.softuni.springsecuritywithjwt.model.entity.Token;
import bg.softuni.springsecuritywithjwt.model.entity.User;
import bg.softuni.springsecuritywithjwt.model.enums.Role;
import bg.softuni.springsecuritywithjwt.model.enums.TokenType;
import bg.softuni.springsecuritywithjwt.repository.TokenRepository;
import bg.softuni.springsecuritywithjwt.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserRepository userRepository, TokenRepository tokenRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        User user = new User()
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setEmail(request.getEmail())
                .setPassword(this.passwordEncoder.encode(request.getPassword()))
                .setRole(Role.USER);

        User savedUser = this.userRepository.save(user);

        String jwtToken = this.jwtService.generateToken(user);

        String refreshToken = this.jwtService.generateRefreshToken(user);

        saveUserToken(savedUser, jwtToken);

        return new AuthenticationResponse(jwtToken, refreshToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = this.userRepository.findByEmail(request.getEmail()).orElseThrow();

        String jwtToken = this.jwtService.generateToken(user);

        String refreshToken = this.jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);

        saveUserToken(user, jwtToken);

        return new AuthenticationResponse(jwtToken, refreshToken);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = this.tokenRepository.findAllValidTokensByUser(user.getId());

        if (validUserTokens.isEmpty()) {
            return;
        }

        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        this.tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = new Token()
                .setUser(user)
                .setToken(jwtToken)
                .setTokenType(TokenType.BEARER)
                .setRevoked(false)
                .setExpired(false);

        this.tokenRepository.save(token);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);

        userEmail = this.jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            User user = this.userRepository.findByEmail(userEmail).orElseThrow();

            if (this.jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = this.jwtService.generateToken(user);

                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                AuthenticationResponse authResponse = new AuthenticationResponse()
                        .setAccessToken(accessToken)
                        .setRefreshToken(refreshToken);

                //returns body of the response
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}