package neobis.mobimaket.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, USER_ACTIVE, USER, REMOVED;

    @Override
    public String getAuthority() {
        return name();
    }
}
