package neobis.mobimaket.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import neobis.mobimaket.entity.User;
import neobis.mobimaket.entity.dto.request.LoginRequest;
import neobis.mobimaket.entity.dto.request.RegistrationRequest;
import neobis.mobimaket.entity.dto.response.LoginResponse;
import neobis.mobimaket.entity.enums.UserState;
import neobis.mobimaket.entity.mapper.AuthMapper;
import neobis.mobimaket.exception.IncorrectLoginException;
import neobis.mobimaket.exception.NotFoundException;
import neobis.mobimaket.repository.UserRepository;
import neobis.mobimaket.security.jwt.JwtTokenUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService{
    UserRepository userRepository;
    BCryptPasswordEncoder encoder;
    JwtTokenUtil jwtTokenUtil;
    AuthMapper authMapper;


    @Override
    public LoginResponse login(LoginRequest request) {
        User existUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found by username = " + request.getUsername()));
        if (encoder.matches(request.getPassword(), existUser.getPassword()) && existUser.getState() == UserState.ACTIVATED) {
            return authMapper.loginView(jwtTokenUtil.generateToken(existUser), jwtTokenUtil.generateRefreshToken(existUser), existUser);
        } else {
            throw new IncorrectLoginException("Password is not correct or Access denied! You are not registered");
        }
    }

    @Override
    public String registration(RegistrationRequest request) {
        return null;
    }

    @Override
    public String registrationConfirm(String code) {
        return null;
    }

    @Override
    public String sendMessage(RegistrationRequest request) {
        return null;
    }
}
