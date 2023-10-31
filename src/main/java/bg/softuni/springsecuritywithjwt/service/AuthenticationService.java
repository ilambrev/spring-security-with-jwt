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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        saveUserToken(savedUser, jwtToken);

        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = this.userRepository.findByEmail(request.getEmail()).orElseThrow();

        String jwtToken = this.jwtService.generateToken(user);

        revokeAllUserTokens(user);

        saveUserToken(user, jwtToken);

        return new AuthenticationResponse(jwtToken);
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

}