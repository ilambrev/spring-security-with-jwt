package bg.softuni.springsecuritywithjwt.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    public AuthenticationResponse() {

    }

    public AuthenticationResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public AuthenticationResponse setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public AuthenticationResponse setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

}