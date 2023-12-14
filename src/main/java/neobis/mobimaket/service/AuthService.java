package neobis.mobimaket.service;

import neobis.mobimaket.entity.dto.request.RefreshTokenRequest;
import neobis.mobimaket.entity.dto.request.LoginRequest;
import neobis.mobimaket.entity.dto.response.LoginResponse;
import neobis.mobimaket.entity.dto.request.RegistrationRequest;
import neobis.mobimaket.entity.dto.response.RefreshTokenResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    String registration(RegistrationRequest request);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
}
