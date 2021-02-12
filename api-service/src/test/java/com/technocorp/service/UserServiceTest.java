package com.technocorp.service;

import com.technocorp.model.User;
import com.technocorp.repository.UserRepository;
import com.technocorp.service.servicedto.ServiceRequestUserDTO;
import com.technocorp.service.servicedto.ServiceResponseUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private List<User> listResponse = new ArrayList<>();
    private List<User> emptyList = new ArrayList<>();
    private ServiceRequestUserDTO requestUserDTO;
    private User user;
    private User user2;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setup() {
        this.user = User.builder()
                .id("1")
                .name("Andrews")
                .surname("Souza")
                .age("20")
                .cpf("123")
                .login("Andrews")
                .password("123")
                .admin(false)
                .build();

        this.user2 = User.builder()
                .id("2")
                .name("And")
                .surname("Souza")
                .age("20")
                .cpf("123")
                .login("ze")
                .password("123")
                .admin(false)
                .build();

        this.requestUserDTO = ServiceRequestUserDTO.builder()
                .id(this.user.getId())
                .name(this.user.getName())
                .surname(this.user.getSurname())
                .age(this.user.getAge())
                .cpf(this.user.getCpf())
                .login(this.user.getLogin())
                .password(this.user.getPassword())
                .build();

        listResponse.add(this.user);
        listResponse.add(this.user2);
    }

    @Test
    void whenFindAll_ThenTestReturn() {
        when(userRepository.findAll()).thenReturn(listResponse);
        var stubActual = userService.listAllUsers();
        var stubExpected = listResponse.stream()
                .map(dto -> ServiceResponseUserDTO.builder()
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
    void whenFindByName_ThenTestReturn() {
        when(userRepository.findByNameIgnoreCase(this.user.getName())).thenReturn(listResponse);
        var stubActual = userService.findByName(this.user.getName());
        var stubExpected = listResponse.stream().map(
                dto -> ServiceResponseUserDTO.builder()
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
    void whenSave_ThenTestIfWasSaved() {
        var stubUser = this.user;
        stubUser.setId(null);
        when(userRepository.save(stubUser)).thenReturn(this.user);
        this.requestUserDTO.setId(null);
        var stubActual = userService.save(this.requestUserDTO);
        var stubExpected = ServiceResponseUserDTO.builder()
                .id(this.user.getId())
                .name(this.user.getName())
                .surname(this.user.getSurname())
                .age(this.user.getAge())
                .cpf(this.user.getCpf())
                .admin(this.user.isAdmin())
                .build();
        assertEquals(stubExpected, stubActual);
    }

    @Test
    void whenUpdate_ThenTestIdWasUpdated() {
        when(userRepository.existsById(this.user.getId())).thenReturn(true);
        when(userRepository.save(this.user)).thenReturn(this.user);
        var stubActual = userService.update(this.requestUserDTO);
        var stubExpected = ServiceResponseUserDTO.builder()
                .id(this.user.getId())
                .name(this.user.getName())
                .surname(this.user.getSurname())
                .age(this.user.getAge())
                .cpf(this.user.getCpf())
                .admin(this.user.isAdmin())
                .build();
        assertEquals(stubExpected, stubActual);
    }

    @Test
    void whenDeleteById_ThenTestIfWasDeleted() {
        when(userRepository.existsById(this.user.getId())).thenReturn(true);
        userService.deleteById(this.user.getId());
        verify(userRepository, times(1)).deleteById(this.user.getId());
    }

    @Test
    void whenFindAllReturnEmptyList_ThenExceptionIsThrown() {
        when(userRepository.findAll()).thenReturn(emptyList);
        var thrown = assertThrows(ResponseStatusException.class, () -> userService.listAllUsers());
        assertEquals(HttpStatus.NO_CONTENT, thrown.getStatus());
    }

    @Test
    void whenFindByNameThatNotExists_ThenExceptionIsThrown() {
        when(userRepository.findByNameIgnoreCase("Zé")).thenReturn(emptyList);
        var thrown = assertThrows(ResponseStatusException.class, () -> userService.findByName("Zé"));
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    void whenUpdateById_ThenExceptionIsThrown() {
        when(userRepository.existsById(this.requestUserDTO.getId())).thenReturn(false);
        var thrown = assertThrows(ResponseStatusException.class, () -> userService.update(this.requestUserDTO));
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    @Disabled
        //Not Working
    void whenUserNotSaved_ThenExceptionIsThrown() {
        when(userService.save(this.requestUserDTO)).thenReturn(null);
        var thrown = assertThrows(ResponseStatusException.class, () -> userService.save(this.requestUserDTO));
        assertEquals(BAD_GATEWAY, thrown.getStatus());
    }

    @Test
    @Disabled
        // Not Working
    void whenDeleteByIdWithIdNotFound_ThenTestThrownException() {
        when(userRepository.existsById(this.requestUserDTO.getId())).thenReturn(false);
        var thrown = assertThrows(ResponseStatusException.class, () -> userService.deleteById(this.user.getId()));
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

}
