package bg.softuni.springsecuritywithjwt.service;

import bg.softuni.springsecuritywithjwt.model.dto.AuthenticationRequest;
import bg.softuni.springsecuritywithjwt.model.dto.AuthenticationResponse;
import bg.softuni.springsecuritywithjwt.model.dto.RegisterRequest;
import bg.softuni.springsecuritywithjwt.model.entity.User;
import bg.softuni.springsecuritywithjwt.model.enums.Role;
import bg.softuni.springsecuritywithjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
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

        this.userRepository.save(user);

        String jwtToken = this.jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = this.userRepository.findByEmail(request.getEmail()).orElseThrow();

        String jwtToken = this.jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }

}