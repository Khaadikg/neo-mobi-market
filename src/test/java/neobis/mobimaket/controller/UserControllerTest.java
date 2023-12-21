package neobis.mobimaket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import neobis.mobimaket.entity.dto.request.SendCodeRequest;
import neobis.mobimaket.entity.dto.request.UserRequest;
import neobis.mobimaket.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserControllerTest {
    UserService userService;
    MockMvc mockMvc;
    ObjectMapper mapper;
    String URL = "/api/user";

    @Autowired
    public UserControllerTest(ObjectMapper mapper, WebApplicationContext webApplicationContext,
                              UserService userService) {
        this.mapper = mapper;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.userService = userService;
    }

    @Test
    @WithMockUser(authorities = {"USER_ACTIVE"})
    void phoneConfirm() throws Exception{
        SendCodeRequest request = SendCodeRequest.builder().username("regis_username").phone("0550550550").build();
        String code = userService.sendMessage(request);
        this.mockMvc.perform(MockMvcRequestBuilders.put(URL + "/phone-confirm")
                        .param("code", code)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"USER_ACTIVE"})
    void resendMessage() throws Exception{
        SendCodeRequest request = SendCodeRequest.builder().username("regis_username").phone("0550550550").build();
        this.mockMvc.perform(MockMvcRequestBuilders.put(URL + "/send-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"USER_ACTIVE"}, username = "regis_username")
    void getAllPersonalProducts() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL + "/get-personal-products"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"USER_ACTIVE"}, username = "regis_username")
    void getAllLikedProducts() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL + "/get-liked-products"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"USER_ACTIVE"}, username = "regis_username")
    void likeProduct() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.put(URL + "/like")
                .param("id", "1"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"USER_ACTIVE"}, username = "regis_username")
    void updateProfile() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(UserRequest.builder()
                                        .name("name")
                                        .surname("surname")
                                        .lastname("lastname")
                                        .birthDate("24.09.2003")
                                .build())))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.ALL))
                .andExpect(status().isOk());
    }
}