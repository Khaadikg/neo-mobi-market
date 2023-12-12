package neobis.mobimaket.entity.mapper;

import neobis.mobimaket.entity.User;
import neobis.mobimaket.entity.dto.response.LoginResponse;

public class AuthMapper {
    public LoginResponse loginView(String token, String refreshToken, User user) {
        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .authorities(user.getAuthorities().toString())
                .build();
    }
}
