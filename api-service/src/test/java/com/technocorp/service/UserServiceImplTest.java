package com.technocorp.service;

import com.technocorp.model.User;
import com.technocorp.repository.UserRepository;
import com.technocorp.util.dto.ServiceRequestUserDTO;
import com.technocorp.util.dto.ServiceResponseUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private ServiceRequestUserDTO requestUserDTO;
    private User user;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userServiceImpl;

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
    void whenFindAllShouldReturnAListOfUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(this.user));
        var stubActual = userServiceImpl.findAll();
        var stubExpected = Stream.of(this.user)
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
    void whenFindByNameShouldReturnAListOfUsersThatMatchTheName() {
        when(userRepository.findByNameIgnoreCaseContaining(this.user.getName())).thenReturn(Collections.singletonList(this.user));
        var stubActual = userServiceImpl.findByName(this.user.getName());
        var stubExpected = Stream.of(this.user)
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
    void whenSaveShouldReturnTheSavedUser() {
        var stubUser = this.user;
        stubUser.setId(null);
        when(userRepository.save(stubUser)).thenReturn(this.user);
        this.requestUserDTO.setId(null);
        var stubActual = userServiceImpl.save(this.requestUserDTO);
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
    void whenUpdateShouldReturnTheUpdatedUser() {
        when(userRepository.existsById(this.user.getId())).thenReturn(true);
        when(userRepository.save(this.user)).thenReturn(this.user);
        var stubActual = userServiceImpl.update("1", this.requestUserDTO);
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
    void whenUpdateShouldThrowAnException() {
        when(userRepository.existsById(this.user.getId())).thenReturn(false);
        var thrown = assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.update("1", this.requestUserDTO));
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    @Disabled("Logic Changed")
    void whenFindAllReturnEmptyListShouldThrownException() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        var thrown = assertThrows(ResponseStatusException.class, () -> userServiceImpl.findAll());
        assertEquals(HttpStatus.NO_CONTENT, thrown.getStatus());
    }

    @Test
    @Disabled("Logic Changed")
    void whenFindByNameThatNotExistsShouldThrownException() {
        when(userRepository.findByNameIgnoreCaseContaining("Zé")).thenReturn(Collections.emptyList());
        var thrown = assertThrows(ResponseStatusException.class, () -> userServiceImpl.findByName("Zé"));
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    void whenDeleteByIdShouldReturnNothing() {
        when(userRepository.existsById(this.user.getId())).thenReturn(true);
        userServiceImpl.deleteById(this.user.getId());
        verify(userRepository, times(1)).deleteById(this.user.getId());
    }

    @Test
    void whenDeleteByIdNotFoundShouldThrownException() {
        when(userRepository.existsById(this.user.getId())).thenReturn(false);
        var thrown = assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.deleteById("1"));
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

}
