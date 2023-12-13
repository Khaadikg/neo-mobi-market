package neobis.mobimaket.entity.mapper;

import neobis.mobimaket.entity.User;
import neobis.mobimaket.entity.dto.request.RegistrationRequest;
import neobis.mobimaket.entity.dto.response.LoginResponse;
import neobis.mobimaket.entity.enums.Role;

public class AuthMapper {
    public static LoginResponse loginView(String token, String refreshToken, User user) {
        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .authorities(user.getAuthorities().toString())
                .build();
    }

    public static User mapUserRequestToUser(RegistrationRequest request) {
        return User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .role(Role.USER)
                .build();
    }
}
