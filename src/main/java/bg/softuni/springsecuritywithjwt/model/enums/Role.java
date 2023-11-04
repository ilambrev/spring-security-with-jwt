package bg.softuni.springsecuritywithjwt.model.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static bg.softuni.springsecuritywithjwt.model.enums.Permission.*;

public enum Role {

    ADMIN(Set.of(ADMIN_READ, ADMIN_CREATE, ADMIN_UPDATE, ADMIN_DELETE,
            MANAGER_READ, MANAGER_CREATE, MANAGER_UPDATE, MANAGER_DELETE)),
    MANAGER(Set.of(MANAGER_READ, MANAGER_CREATE, MANAGER_UPDATE, MANAGER_DELETE)),
    USER(Collections.emptySet());

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .toList();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return  authorities;
    }

}
