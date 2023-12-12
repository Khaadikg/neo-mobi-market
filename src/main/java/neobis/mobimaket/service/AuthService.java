package neobis.mobimaket.service;

import neobis.mobimaket.entity.dto.request.LoginRequest;
import neobis.mobimaket.entity.dto.request.SendCodeRequest;
import neobis.mobimaket.entity.dto.response.LoginResponse;
import neobis.mobimaket.entity.dto.request.RegistrationRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    String registration(RegistrationRequest request);
    String registrationConfirm(Integer token, String username);
    String sendMessage(SendCodeRequest request);
    String refreshToken(String token, String username);
}
