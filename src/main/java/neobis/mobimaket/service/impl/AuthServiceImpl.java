package neobis.mobimaket.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import neobis.mobimaket.entity.User;
import neobis.mobimaket.entity.dto.request.LoginRequest;
import neobis.mobimaket.entity.dto.request.RegistrationRequest;
import neobis.mobimaket.entity.dto.request.SendCodeRequest;
import neobis.mobimaket.entity.dto.response.LoginResponse;
import neobis.mobimaket.entity.enums.UserState;
import neobis.mobimaket.entity.mapper.AuthMapper;
import neobis.mobimaket.exception.*;
import neobis.mobimaket.repository.UserRepository;
import neobis.mobimaket.security.jwt.JwtTokenUtil;
import neobis.mobimaket.service.AuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    BCryptPasswordEncoder encoder;
    JwtTokenUtil jwtTokenUtil;
    SmsSender smsSender;

    @Override
    public LoginResponse login(LoginRequest request) {
        User existUser = getUserByUsername(request.getUsername());
        if (encoder.matches(request.getPassword(), existUser.getPassword()) && existUser.getState() == UserState.ACTIVATED) {
            return AuthMapper.loginView(jwtTokenUtil.generateToken(existUser), jwtTokenUtil.generateRefreshToken(existUser), existUser);
        } else {
            throw new IncorrectLoginException("Password is not correct or Access denied! You are not registered");
        }
    }

    @Override
    public String registration(RegistrationRequest request) {
        if (userRepository.findByUniqConstraint(request.getUsername(), request.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User with username = " + request.getEmail() + " already exist");
        }
        User user = AuthMapper.mapUserRequestToUser(request);
        user.setPassword(encoder.encode(request.getPassword()));
        userRepository.save(user);
        return "User successfully saved!";
    }

    @Override
    public String registrationConfirm(Integer token, String username) {
        User user = getUserByUsername(username);
        if (!user.getToken().equals(token)) {
            throw new IncorrectCodeException("Code is not correct");
        }
        if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Your code got expired send new one!");
        }
        user.setState(UserState.ACTIVATED);
        user.setToken(0);
        user.setTokenExpiration(null);
        userRepository.save(user);
        return "User successfully activated!";
    }

    @Override
    public String sendMessage(SendCodeRequest request) {
        Random random =  new Random();
        Integer token = random.nextInt(1000, 9999);
        User user = getUserByUsername(request.getUsername());
        user.setToken(token);
        user.setTokenExpiration(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);
//        return String.valueOf(token);
        return smsSender.sendSms(request, String.valueOf(token));
    }

    @Override
    public String refreshToken(String refreshToken, String username) {
        User user = getUserByUsername(username);
        if (jwtTokenUtil.validationToken(refreshToken, user)) {
            return jwtTokenUtil.generateToken(user);
        }
        throw new TokenExpiredException("Your refresh token got expired!");
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found by username = " + username));
    }
}
