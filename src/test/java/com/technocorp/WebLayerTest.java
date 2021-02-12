package com.technocorp;

import com.technocorp.controller.UserController;
import com.technocorp.model.User;
import com.technocorp.service.UserService;
import com.technocorp.service.servicedto.ServiceResponseUserDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserController.class, UserService.class})
@AutoConfigureRestDocs(outputDir = "build/snippets")
class WebLayerTest {

    private List<User> listResponseUser = new ArrayList<>();
    private ServiceResponseUserDTO responseUserDTO;
    private User user;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;


    @BeforeEach
    void setUp() {
        this.user = User.builder()
                .id("1")
                .name("Andrews")
                .surname("Souza")
                .age("30")
                .cpf("123")
                .login("andrews")
                .password("123")
                .admin(false)
                .build();

        this.responseUserDTO = ServiceResponseUserDTO.builder()
                .id(this.user.getId())
                .name(this.user.getName())
                .surname(this.user.getSurname())
                .age(this.user.getAge())
                .cpf(this.user.getCpf())
                .admin(this.user.isAdmin())
                .build();

        listResponseUser.add(this.user);
    }

    @Test
    void findAllShouldReturnOK() throws Exception {

        when(userService.listAllUsers()).thenReturn(Arrays.asList(responseUserDTO));

        this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is("1")))
                .andExpect(jsonPath("$[0].name", Matchers.is("Andrews")))
                .andExpect(jsonPath("$[0].surname", Matchers.is("Souza")))
                .andExpect(jsonPath("$[0].age", Matchers.is("30")))
                .andExpect(jsonPath("$[0].cpf", Matchers.is("123")))
                .andExpect(jsonPath("$[0].admin", Matchers.is(false)))
                .andDo(print())
                .andDo(document("users/findByName",
                        responseFields(
                                fieldWithPath("[0].id").description("The unique identifier of the user, this field can't be set by a normal user."),
                                fieldWithPath("[0].name").description("The name of the user."),
                                fieldWithPath("[0].surname").description("The surname of the user."),
                                fieldWithPath("[0].cpf").description("The \"cadastro de pessoa física\" of the user."),
                                fieldWithPath("[0].age").description("The age of the user."),
                                fieldWithPath("[0].admin").description("If user is an admin, this field can't be set by a normal user.")
                        )
                ));

        verify(userService,times(1)).listAllUsers();
    }
}
