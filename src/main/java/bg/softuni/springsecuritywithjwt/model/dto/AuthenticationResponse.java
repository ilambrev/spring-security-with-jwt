package bg.softuni.springsecuritywithjwt.model.dto;

public class AuthenticationResponse {

    private String token;

    public AuthenticationResponse() {

    }

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public AuthenticationResponse setToken(String token) {
        this.token = token;
        return this;
    }

}