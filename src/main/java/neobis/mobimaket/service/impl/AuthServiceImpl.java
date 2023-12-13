package neobis.mobimaket.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import neobis.mobimaket.entity.User;
import neobis.mobimaket.entity.dto.request.LoginRequest;
import neobis.mobimaket.entity.dto.request.RefreshTokenRequest;
import neobis.mobimaket.entity.dto.request.RegistrationRequest;
import neobis.mobimaket.entity.dto.response.LoginResponse;
import neobis.mobimaket.entity.enums.Role;
import neobis.mobimaket.entity.mapper.AuthMapper;
import neobis.mobimaket.exception.IncorrectLoginException;
import neobis.mobimaket.exception.NotFoundException;
import neobis.mobimaket.exception.TokenExpiredException;
import neobis.mobimaket.exception.UserAlreadyExistException;
import neobis.mobimaket.repository.UserRepository;
import neobis.mobimaket.security.jwt.JwtTokenUtil;
import neobis.mobimaket.service.AuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        if (encoder.matches(request.getPassword(), existUser.getPassword()) && existUser.getRole() != Role.REMOVED) {
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
        user.setRole(Role.USER);
        userRepository.save(user);
        return "User successfully saved!";
    }

    @Override
    public String refreshToken(RefreshTokenRequest request) {
        User user = getUserByUsername(request.getUsername());
        if (jwtTokenUtil.validationToken(request.getToken(), user)) {
            return jwtTokenUtil.generateToken(user);
        }
        throw new TokenExpiredException("Your refresh token got expired!");
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found by username = " + username));
    }
}
