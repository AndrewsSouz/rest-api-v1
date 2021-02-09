package com.technocorp.service;

import com.technocorp.model.dto.UserResponseDTO;
import com.technocorp.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<UserResponseDTO> listAllUsers() {
        var user = userRepository.findAll();
        return user.stream()
                .map(dto -> UserResponseDTO.builder()
                        .name(dto.getName())
                        .surname(dto.getSurname())
                        .age(dto.getAge())
                        .cpf(dto.getCpf())
                        .admin(dto.isAdmin())
                        .build())
                .collect(Collectors.toList());
    }

    public UserResponseDTO findByName(String name) {
        var user = userRepository.findByNameIgnoreCase(name);
        return UserResponseDTO.builder()
                .name(user.getName())
                .age(user.getAge())
                .cpf(user.getCpf())
                .surname(user.getSurname())
                .admin(user.isAdmin())
                .build();
    }
}
