package neobis.mobimaket.service;

import neobis.mobimaket.entity.dto.request.RefreshTokenRequest;
import neobis.mobimaket.entity.dto.request.LoginRequest;
import neobis.mobimaket.entity.dto.response.LoginResponse;
import neobis.mobimaket.entity.dto.request.RegistrationRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    String registration(RegistrationRequest request);
    String refreshToken(RefreshTokenRequest request);
}
