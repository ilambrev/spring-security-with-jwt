package bg.softuni.springsecuritywithjwt.model.entity;

import bg.softuni.springsecuritywithjwt.model.enums.TokenType;
import jakarta.persistence.*;

@Entity
@Table(name = "tokens")
public class Token extends BaseEntity {

    @Column(name = "token")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    private TokenType tokenType;

    @Column(name = "expired")
    private boolean expired;

    @Column(name = "revoked")
    private boolean revoked;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Token() {

    }

    public String getToken() {
        return token;
    }

    public Token setToken(String token) {
        this.token = token;
        return this;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public Token setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public boolean isExpired() {
        return expired;
    }

    public Token setExpired(boolean expired) {
        this.expired = expired;
        return this;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public Token setRevoked(boolean revoked) {
        this.revoked = revoked;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Token setUser(User user) {
        this.user = user;
        return this;
    }

}