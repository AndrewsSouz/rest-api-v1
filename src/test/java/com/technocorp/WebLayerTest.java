package com.technocorp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technocorp.model.User;
import com.technocorp.repository.UserRepository;
import com.technocorp.service.UserServiceImpl;
import com.technocorp.service.servicedto.ServiceRequestUserDTO;
import com.technocorp.service.servicedto.ServiceResponseUserDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureRestDocs(outputDir = "build/snippets")
class WebLayerTest {

    private ServiceResponseUserDTO responseUserDTO;
    private ServiceRequestUserDTO requestUserDTO;
    private User user;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserServiceImpl userServiceImpl;

    @MockBean
    UserRepository userRepository;


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

        this.requestUserDTO = ServiceRequestUserDTO.builder()
                .name(this.user.getName())
                .surname(this.user.getSurname())
                .age(this.user.getAge())
                .cpf(this.user.getCpf())
                .login(this.user.getLogin())
                .password(this.user.getPassword())
                .build();
    }

    @Test
    @DisplayName("Should return all users in the database.")
    void ShouldReturnAListOfUsersAndStatusOK() throws Exception {
        when(this.userServiceImpl.findAll()).thenReturn(Collections.singletonList(this.responseUserDTO));
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
                .andDo(document("users/list_all_users"));
    }

    @Test
    @DisplayName("Should return a list of users that match the name.")
    void whenFindByNameShouldReturnAnUserAndStatuOK() throws Exception {
        when(this.userServiceImpl.findByName(this.user.getName())).thenReturn(Collections.singletonList(this.responseUserDTO));
        this.mockMvc.perform(get("/users/Andrews"))
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
                .andDo(document("users/find_by_name"));
    }

    @Test
    @DisplayName("Should save an user and return it.")
    void whenSaveShouldReturnTheUserSavedAndStatusCreated() throws Exception {
        this.requestUserDTO.setId(null);
        var json = new ObjectMapper().writeValueAsString(this.requestUserDTO);
        when(this.userServiceImpl.save(this.requestUserDTO)).thenReturn(this.responseUserDTO);
        this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is("1")))
                .andExpect(jsonPath("name", Matchers.is("Andrews")))
                .andExpect(jsonPath("surname", Matchers.is("Souza")))
                .andExpect(jsonPath("age", Matchers.is("30")))
                .andExpect(jsonPath("cpf", Matchers.is("123")))
                .andExpect(jsonPath("admin", Matchers.is(false)))
                .andDo(print())
                .andDo(document("users/save",
                        responseFields(
                                fieldWithPath("id").description("The unique identifier of the user, this field can't be set by a normal user."),
                                fieldWithPath("name").description("The name of the user."),
                                fieldWithPath("surname").description("The surname of the user."),
                                fieldWithPath("cpf").description("The \"cadastro de pessoa física\" of the user."),
                                fieldWithPath("age").description("The age of the user."),
                                fieldWithPath("admin").description("If user is an admin, this field can't be set by a normal user.")
                        )
                ));
    }

    @Test
    @DisplayName("Should update an user and return it.")
    void whenUpdateShouldReturnTheUserSavedAndStatusOK() throws Exception {
        var json = new ObjectMapper().writeValueAsString(this.requestUserDTO);
        when(this.userServiceImpl.update("1", this.requestUserDTO)).thenReturn(this.responseUserDTO);
        this.mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", "1")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is("1")))
                .andExpect(jsonPath("name", Matchers.is("Andrews")))
                .andExpect(jsonPath("surname", Matchers.is("Souza")))
                .andExpect(jsonPath("age", Matchers.is("30")))
                .andExpect(jsonPath("cpf", Matchers.is("123")))
                .andExpect(jsonPath("admin", Matchers.is(false)))
                .andDo(print())
                .andDo(document("users/save"));
    }

    @Test
    @DisplayName("Should update an user and return it.")
    void whenDeleteThenShouldReturnVoidAndStatusNOCONTENT() throws Exception {
        this.mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent())
                .andDo(document("users/delete"));
    }

    @Test
    void findAllshouldReturnStatusNOCONTENT() throws Exception {
        var exception = new ResponseStatusException(HttpStatus.NO_CONTENT);
        doThrow(exception).when(userServiceImpl).findAll();
        this.mockMvc.perform(get("/users"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findByNameShouldReturnStatusNOTFOUND() throws Exception {
        var exception = new ResponseStatusException(HttpStatus.NOT_FOUND);
        doThrow(exception).when(userServiceImpl).findByName("a");
        this.mockMvc.perform(get("/users/a"))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveShouldReturnStatusSERVICEUNAVAILABLE() throws Exception {
        var json = new ObjectMapper().writeValueAsString(this.requestUserDTO);
        var exception = new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        doThrow(exception).when(userServiceImpl).save(requestUserDTO);
        this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void updateShouldReturnStatusSERVICEUNAVAILABLE() throws Exception {
        var json = new ObjectMapper().writeValueAsString(this.requestUserDTO);
        var exception = new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        doThrow(exception).when(userServiceImpl).update("1",requestUserDTO);
        this.mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", "1")
                .content(json))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void deleteShouldReturnStatusNOTFOUND() throws Exception {
        var exception = new ResponseStatusException(HttpStatus.NOT_FOUND);
        doThrow(exception).when(userServiceImpl).deleteById("99999");
        this.mockMvc.perform(delete("/users/99999"))
                .andExpect(status().isNotFound());
    }


}
