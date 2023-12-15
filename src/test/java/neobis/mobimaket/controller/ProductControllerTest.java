package neobis.mobimaket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import neobis.mobimaket.entity.dto.request.ProductRequest;
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
class ProductControllerTest {
    MockMvc mockMvc;
    ObjectMapper mapper;
    String URL = "/api/product";

    @Autowired
    public ProductControllerTest(ObjectMapper mapper, WebApplicationContext webApplicationContext) {
        this.mapper = mapper;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(authorities = {"USER_ACTIVE"})
    void saveProduct() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ProductRequest.builder()
                                .name("some_name")
                                .shortDescription("short")
                                .fullDescription("full")
                                .price(123.0)
                                .build())))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"USER_ACTIVE"})
    void getProductById() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .param("id", "1"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"USER_ACTIVE"})
    void updateProduct() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ProductRequest.builder()
                                .name("some_name")
                                .shortDescription("short")
                                .fullDescription("full")
                                .price(123.0)
                                .build())))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void getAllProducts() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL + "/all"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"USER_ACTIVE"}, username = "regis_username")
    void deleteProductById() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL).param("id", String.valueOf(1231231231)))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
