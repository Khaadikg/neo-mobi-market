package neobis.mobimaket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import neobis.mobimaket.entity.User;
import neobis.mobimaket.entity.dto.request.LoginRequest;
import neobis.mobimaket.entity.dto.request.RefreshTokenRequest;
import neobis.mobimaket.entity.dto.request.RegistrationRequest;
import neobis.mobimaket.entity.enums.Role;
import neobis.mobimaket.repository.UserRepository;
import neobis.mobimaket.security.jwt.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AuthControllerTest {
    UserRepository userRepository;
    MockMvc mockMvc;
    ObjectMapper mapper;
    JwtTokenUtil jwtTokenUtil;
    String URL = "/api/auth";
    BCryptPasswordEncoder encoder;
    @Autowired
    public AuthControllerTest(ObjectMapper mapper, WebApplicationContext webApplicationContext,
                              UserRepository userRepository, BCryptPasswordEncoder encoder, JwtTokenUtil jwtTokenUtil) {
        this.mapper = mapper;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.encoder = encoder;
    }
    @Test
    void registration() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL + "/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(RegistrationRequest.builder()
                                .email("notexist@mail.com")
                                .password("regis_password")
                                .username("regis_username")
                                .build())))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    void login() throws Exception{
        if (userRepository.findByUsername("some_username_valid").isEmpty()) {
            userRepository.save(User.builder()
                    .role(Role.USER)
                    .tokenExpiration(LocalDateTime.now().plusMinutes(5))
                    .username("some_username")
                    .password(encoder.encode("some_password"))
                    .build());
        }
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL + "/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(LoginRequest.builder()
                                .password("some_password")
                                .username("some_username")
                                .build())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getRefreshToken() throws Exception{
        User user = User.builder()
                .role(Role.USER)
                .tokenExpiration(LocalDateTime.now().plusMinutes(5))
                .username("some_username")
                .password(encoder.encode("some_password"))
                .build();
        if (userRepository.findByUsername("some_username").isEmpty()) {
            userRepository.save(user);
        }
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL + "/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(RefreshTokenRequest.builder()
                                .token(jwtTokenUtil.generateToken(user))
                                .username("some_username")
                                .build())))
                .andDo(print())
                .andExpect(status().isOk());
    }
}