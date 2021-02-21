package com.technocorp.controller;

import com.technocorp.controller.dto.ControllerRequestUserDTO;
import com.technocorp.controller.dto.ControllerResponseUserDTO;
import com.technocorp.model.User;
import com.technocorp.service.UserServiceImpl;
import com.technocorp.service.servicedto.ServiceRequestUserDTO;
import com.technocorp.service.servicedto.ServiceResponseUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private ControllerRequestUserDTO requestUserDTO;
    private ControllerResponseUserDTO responseUserDTO;
    private ServiceRequestUserDTO serviceRequestUserDTO;
    private ServiceResponseUserDTO serviceResponseUserDTO;

    @Mock
    UserServiceImpl userServiceImpl;

    @InjectMocks
    UserController userController;

    @BeforeEach
    void setup() {

        User user = User.builder()
                .id("1")
                .name("Andrews")
                .surname("Souza")
                .age("20")
                .cpf("123")
                .login("andrews")
                .password("123")
                .admin(false)
                .build();

        this.requestUserDTO = ControllerRequestUserDTO.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .cpf(user.getCpf())
                .login(user.getLogin())
                .password(user.getPassword())
                .build();

        this.responseUserDTO = ControllerResponseUserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .cpf(user.getCpf())
                .build();

        this.serviceRequestUserDTO = ServiceRequestUserDTO.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .cpf(user.getCpf())
                .login(user.getLogin())
                .password(user.getPassword())
                .build();

        this.serviceResponseUserDTO = ServiceResponseUserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .cpf(user.getCpf())
                .build();

    }


    @Test
    @DisplayName("Should return a list of users")
    void whenFindAllshouldReturnAListOfUsers() {
        when(userServiceImpl.findAll()).thenReturn(Arrays.asList(serviceResponseUserDTO));
        var stubActual = userController.listAllUsers();
        var stubExpected = Arrays.asList(this.responseUserDTO).stream()
                .map(dto -> ControllerResponseUserDTO.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .surname(dto.getSurname())
                        .age(dto.getAge())
                        .cpf(dto.getCpf())
                        .admin(dto.isAdmin())
                        .build())
                .collect(Collectors.toList());
        assertEquals(stubExpected, stubActual);
    }

    @Test
    @DisplayName("Should return a list of users that match the name")
    void whenFindByIdShouldReturnAListOfUsersThatMatchTheName() {
        when(userServiceImpl.findByName(this.requestUserDTO.getName())).thenReturn(Arrays.asList(serviceResponseUserDTO));
        var stubActual = userController.findByName(this.requestUserDTO.getName());
        var stubExpected = Arrays.asList(this.responseUserDTO).stream()
                .map(dto -> ControllerResponseUserDTO.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .surname(dto.getSurname())
                        .age(dto.getAge())
                        .cpf(dto.getCpf())
                        .admin(dto.isAdmin())
                        .build())
                .collect(Collectors.toList());
        assertEquals(stubExpected, stubActual);
    }

    @Test
    @DisplayName("Should return the saved user")
    void whenSaveShouldReturnTheUserSaved() {
        when(userServiceImpl.save(this.serviceRequestUserDTO)).thenReturn(this.serviceResponseUserDTO);
        var stubActual = userController.save(this.requestUserDTO);
        var stubExpected = ControllerResponseUserDTO.builder()
                .id(this.serviceResponseUserDTO.getId())
                .name(this.serviceResponseUserDTO.getName())
                .surname(this.serviceResponseUserDTO.getSurname())
                .age(this.serviceResponseUserDTO.getAge())
                .cpf(this.serviceResponseUserDTO.getCpf())
                .admin(this.serviceResponseUserDTO.isAdmin())
                .build();
        assertEquals(stubExpected, stubActual);
    }

    @Test
    @DisplayName("Should return the updated user")
    void whenUpdateShouldReturnTheUserUpdated() {
        when(userServiceImpl.update("1", this.serviceRequestUserDTO)).thenReturn(this.serviceResponseUserDTO);
        var stubActual = userController.update("1", this.requestUserDTO);
        var stubExpected = ControllerResponseUserDTO.builder()
                .id(this.responseUserDTO.getId())
                .name(this.responseUserDTO.getName())
                .surname(this.responseUserDTO.getSurname())
                .age(this.responseUserDTO.getAge())
                .cpf(this.responseUserDTO.getCpf())
                .admin(this.responseUserDTO.isAdmin())
                .build();
        assertEquals(stubExpected, stubActual);
    }

    @Test
    @DisplayName("Should verify if the delete method is acessed")
    void whenDeleteByIdShouldReturnNothing() {
        userController.deleteById("1");
        verify(userServiceImpl, times(1)).deleteById("1");
    }


}
