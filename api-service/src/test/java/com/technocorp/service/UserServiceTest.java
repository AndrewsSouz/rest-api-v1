package com.technocorp.service;

import com.technocorp.model.User;
import com.technocorp.model.dto.UserResponseDTO;
import com.technocorp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private User user;
    private User user2;


    @BeforeEach
    void setup() {
        user = User.builder()
                .name("Andrews")
                .surname("Souza")
                .age("20")
                .cpf("123")
                .admin(false)
                .build();

        user2 = User.builder()
                .name("Zé")
                .surname("Souza")
                .age("20")
                .cpf("123")
                .admin(false)
                .build();
    }

    @Test
    void whenFindAll_ThenTestReturn() {
        List<User> response = new ArrayList<>();
        response.add(user);
        response.add(user2);

        //stub do repository
        when(userRepository.findAll()).thenReturn(response);

        var stubActual = userService.listAllUsers();
        //conversão para DTO só é feita no service, então pego o retorno do repository e transformo em DTO para passar no teste.
        var stubExpected = response.stream()
                .map(x-> new UserResponseDTO(
                        x.getName(),
                        x.getSurname(),
                        x.getAge(),
                        x.getCpf(),
                        x.isAdmin()))
                .collect(Collectors.toList());

        Assertions.assertEquals(stubExpected,stubActual);
    }

    @Test
    void whenFindByName_ThenTestReturn(){
        when(userRepository.findByNameIgnoreCase("andrews")).thenReturn(user);
    }


}
