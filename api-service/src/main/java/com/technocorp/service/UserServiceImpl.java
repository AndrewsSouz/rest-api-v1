package com.technocorp.service;

import com.technocorp.repository.UserRepository;
import com.technocorp.util.dto.ServiceRequestUserDTO;
import com.technocorp.util.dto.ServiceResponseUserDTO;
import com.technocorp.util.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public List<ServiceResponseUserDTO> findAll() {
        return Optional.ofNullable(
                userRepository.findAll().stream()
                        .map(Mapper.toServiceResponseUserDTO)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new ResponseStatusException(NO_CONTENT, "Sorry the database is empty!"));
    }

    public List<ServiceResponseUserDTO> findByName(String name) {
        return Optional.ofNullable(
                userRepository.findByNameIgnoreCaseContaining(name).stream()
                        .map(Mapper.toServiceResponseUserDTO)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found!"));
    }

    public ServiceResponseUserDTO save(ServiceRequestUserDTO requestDTO) {
        return Optional.ofNullable(
                Mapper.toServiceResponseUserDTO.apply(
                        userRepository.save(Mapper.toUserSave.apply(requestDTO))))
                .orElseThrow(() -> new ResponseStatusException(SERVICE_UNAVAILABLE, "Unreachable server!"));
    }

    public ServiceResponseUserDTO update(String id, ServiceRequestUserDTO requestDTO) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Id not found");
        }
        return Optional.ofNullable(
                Mapper.toServiceResponseUserDTO.apply(
                        userRepository.save(
                                Mapper.toUserUpdate.apply(id, requestDTO))))
                .orElseThrow(() -> new ResponseStatusException(SERVICE_UNAVAILABLE, "Unreachable server!"));
    }

    public void deleteById(String id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "User to delete not found!");
        }
        userRepository.deleteById(id);
    }

}
