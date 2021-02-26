package com.technocorp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technocorp.controller.dto.ControllerRequestUserDTO;
import com.technocorp.controller.dto.ControllerResponseUserDTO;
import com.technocorp.model.User;
import com.technocorp.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ApiApplicationTests {

    User user;
    ControllerResponseUserDTO responseUserDTO;
    ControllerRequestUserDTO requestUserDTO;

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
        //Request;
        var result = this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestUserDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        //Parsing response and setting the expected response id to the id created by the database;
        var databaseResponseObject = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                ControllerResponseUserDTO.class);
        this.responseUserDTO.setId(databaseResponseObject.getId());
        //Parsing the asserts to strings;
        var databaseResponse = result.getResponse().getContentAsString();
        var expectedResponse = objectMapper.writeValueAsString(this.responseUserDTO);
        assertEquals(expectedResponse, databaseResponse);
    }

    @Test
    void shouldReturnAllUsersOfDatabase() throws Exception {
        //Request;
        var result = this.mockMvc.perform(get("/users"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //Parsing response to a List<object> and setting the expected response object id to the id created by the database;
        var databaseResponseAsObject = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(),
                ControllerResponseUserDTO[].class));
        this.responseUserDTO.setId(databaseResponseAsObject.get(0).getId());
        //Parsing the asserts to strings;
        var databaseResponse = objectMapper.writeValueAsString(Collections.singletonList(databaseResponseAsObject.get(0)));
        var expectedResponse = objectMapper.writeValueAsString(Collections.singletonList(this.responseUserDTO));
        assertEquals(expectedResponse, databaseResponse);
    }


    @Test
    void shouldReturnUsersThatMatchCriteriaOfDatabase() throws Exception {
        //Request
        var result = mockMvc.perform(get("/users/Teste"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //Parsing response to a List<object> and setting the expected response object id to the id created by the database;
        var databaseResponseAsObject = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(),
                ControllerResponseUserDTO[].class));
        this.responseUserDTO.setId(databaseResponseAsObject.get(0).getId());
        //Parsing the asserts to strings;
        var databaseResponse = objectMapper.writeValueAsString(Collections.singletonList(databaseResponseAsObject.get(0)));
        var expectedResponse = objectMapper.writeValueAsString(Collections.singletonList(this.responseUserDTO));
        assertEquals(expectedResponse, databaseResponse);
    }

    @Test
    void shouldReturnTheUpdatedUser() throws Exception {
        var userToBeUpdated = userService.findAll();
        String id = userToBeUpdated.get(0).getId();
        //Request;
        var result = this.mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", id)
                .content(objectMapper.writeValueAsString(requestUserDTO)))
                .andExpect(status().isOk())
                .andReturn();
        //Parsing response and setting the expected response id to the id created by the database;
        var databaseResponseObject = objectMapper.readValue(result.getResponse().getContentAsString(),
                ControllerResponseUserDTO.class);
        this.responseUserDTO.setId(databaseResponseObject.getId());
        //Parsing the asserts to strings;
        var databaseResponse = result.getResponse().getContentAsString();
        var expectedResponse = objectMapper.writeValueAsString(this.responseUserDTO);
        assertEquals(expectedResponse, databaseResponse);
    }

    @Test
    void shouldDeleteAUserAndReturnNOCONTENT() throws Exception {
        var userToBeDeleted = userService.findAll();
        String id = userToBeDeleted.get(userToBeDeleted.size() - 1).getId();
        //Request;
        var result = mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
