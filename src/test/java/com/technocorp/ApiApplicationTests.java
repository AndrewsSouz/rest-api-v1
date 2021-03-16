package com.technocorp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technocorp.controller.UserController;
import com.technocorp.util.dto.ControllerRequestUserDTO;
import com.technocorp.util.dto.ControllerResponseUserDTO;
import com.technocorp.model.User;
import com.technocorp.repository.UserRepository;
import com.technocorp.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(
        classes = {ApiApplication.class, UserController.class,
                UserServiceImpl.class})
class ApiApplicationTests {

    User user;
    ControllerResponseUserDTO responseUserDTO;
    ControllerRequestUserDTO requestUserDTO;

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.user = User.builder()
                .id("1")
                .name("Teste")
                .surname("do Teste")
                .age("99")
                .cpf("999")
                .login("login")
                .password("123")
                .admin(false)
                .build();
        this.responseUserDTO = ControllerResponseUserDTO.builder()
                .id(this.user.getId())
                .name(this.user.getName())
                .surname(this.user.getSurname())
                .age(this.user.getAge())
                .cpf(this.user.getCpf())
                .admin(this.user.isAdmin())
                .build();
        this.requestUserDTO = ControllerRequestUserDTO.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .cpf(user.getCpf())
                .login(user.getLogin())
                .password(user.getPassword())
                .build();
    }

    @Test
    void shouldReturnTheCreatedUser() throws Exception {
        this.user.setId(null);
        when(userRepository.save(this.user)).thenReturn(this.user);
        //stub
        //Request;
        var result = this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(this.requestUserDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        //Parsing response and setting the expected response id to the expected id;
        var databaseResponseObject = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ControllerResponseUserDTO.class);
        databaseResponseObject.setId("1");
        //Parsing the asserts to strings;
        var databaseResponse = objectMapper.writeValueAsString(databaseResponseObject);
        var expectedResponse = objectMapper.writeValueAsString(this.responseUserDTO);
        assertEquals(expectedResponse, databaseResponse);
    }

    @Test
    void shouldReturnAllUsersOfDatabase() throws Exception {
        //stub
        when(userRepository.findAll()).thenReturn(List.of(this.user));
        //Request;
        var result = this.mockMvc.perform(get("/users"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //Parsing response to a List<object> and setting the expected response object id to the id created by the database;
        var databaseResponseAsObject = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(),
                ControllerResponseUserDTO[].class));
        databaseResponseAsObject.get(0).setId("1");
        //Parsing the asserts to strings;
        var databaseResponse = objectMapper.writeValueAsString(Collections.singletonList(databaseResponseAsObject.get(0)));
        var expectedResponse = objectMapper.writeValueAsString(Collections.singletonList(this.responseUserDTO));
        assertEquals(expectedResponse, databaseResponse);
    }


    @Test
    void shouldReturnUsersThatMatchCriteriaOfDatabase() throws Exception {
        //stub
        when(userRepository.findByNameIgnoreCaseContaining("Teste")).thenReturn(List.of(this.user));
        //Request
        var result = mockMvc.perform(get("/users/Teste"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //Parsing response to a List<object> and setting the expected response object id to the id created by the database;
        var databaseResponseAsObject = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(),
                ControllerResponseUserDTO[].class));
        databaseResponseAsObject.get(0).setId("1");
        //Parsing the asserts to strings;
        var databaseResponse = objectMapper.writeValueAsString(Collections.singletonList(databaseResponseAsObject.get(0)));
        var expectedResponse = objectMapper.writeValueAsString(Collections.singletonList(this.responseUserDTO));
        assertEquals(expectedResponse, databaseResponse);
    }

    @Test
    void shouldReturnTheUpdatedUser() throws Exception {
        //stub
        when(userRepository.save(this.user)).thenReturn(this.user);
        when(userRepository.existsById("1")).thenReturn(true);
        //Request;
        var result = this.mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", "1")
                .content(objectMapper.writeValueAsString(requestUserDTO)))
                .andExpect(status().isOk())
                .andReturn();
        //Parsing response and setting the expected response id to the id created by the database;
        var databaseResponseObject = objectMapper.readValue(result.getResponse().getContentAsString(),
                ControllerResponseUserDTO.class);
        databaseResponseObject.setId("1");
        //Parsing the asserts to strings;
        var databaseResponse = result.getResponse().getContentAsString();
        var expectedResponse = objectMapper.writeValueAsString(this.responseUserDTO);
        assertEquals(expectedResponse, databaseResponse);
    }

    @Test
    void shouldDeleteAUserAndReturnNOCONTENT() throws Exception {
        when(userRepository.existsById("1")).thenReturn(true);
        mockMvc.perform(delete("/users/" + "1"))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(userRepository, times(1)).deleteById("1");
    }
}
