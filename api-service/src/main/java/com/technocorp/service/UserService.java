package com.technocorp.service;

import com.technocorp.repository.UserRepository;
import com.technocorp.service.servicedto.MapperDTO;
import com.technocorp.service.servicedto.ServiceRequestUserDTO;
import com.technocorp.service.servicedto.ServiceResponseUserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<ServiceResponseUserDTO> listAllUsers() {
        var user = userRepository.findAll();
        var response = user.stream().map(
                dto -> Optional.ofNullable(MapperDTO.toServiceResponseUserDTO(dto))
                        .orElse(new ServiceResponseUserDTO()))
                .collect(Collectors.toList());
        if (response.isEmpty()) {
            throw new ResponseStatusException(NO_CONTENT);
        }
        return response;
    }

    public List<ServiceResponseUserDTO> findByName(String name) {
        var user = userRepository.findByNameIgnoreCase(name);
        var response = user.stream().map(
                dto -> Optional.ofNullable(MapperDTO.toServiceResponseUserDTO(dto))
                        .orElse(new ServiceResponseUserDTO()))
                .collect(Collectors.toList());
        if (response.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND);
        }
        return response;
    }

    public ServiceResponseUserDTO save(ServiceRequestUserDTO requestDTO) {
        var requestUser = MapperDTO.toUserSave(requestDTO);
        var responseUser = userRepository.save(requestUser);

        return Optional.ofNullable(MapperDTO.toServiceResponseUserDTO(responseUser))
                .orElseThrow(() -> new ResponseStatusException(SERVICE_UNAVAILABLE));
    }

    public ServiceResponseUserDTO update(ServiceRequestUserDTO requestDTO) {
        var exists = userRepository.existsById(requestDTO.getId());
        if (!exists) {
            throw new ResponseStatusException(NOT_FOUND);
        }

        var requestUser = MapperDTO.toUserUpdate(requestDTO);
        var responseUser = userRepository.save(requestUser);

        return Optional.ofNullable(MapperDTO.toServiceResponseUserDTO(responseUser))
                .orElseThrow(() -> new ResponseStatusException(SERVICE_UNAVAILABLE));
    }

    public void deleteById(String id) {
        var exists = userRepository.existsById(id);
        if (!exists) {
            throw new ResponseStatusException(NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

}
