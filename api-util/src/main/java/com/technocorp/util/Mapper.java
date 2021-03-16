package com.technocorp.util;

import com.technocorp.model.User;
import com.technocorp.util.dto.ControllerRequestUserDTO;
import com.technocorp.util.dto.ControllerResponseUserDTO;
import com.technocorp.util.dto.ServiceRequestUserDTO;
import com.technocorp.util.dto.ServiceResponseUserDTO;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class Mapper {

    private Mapper(){}

    public static final Function<ControllerRequestUserDTO, ServiceRequestUserDTO> toServiceRequestUserDTO =
            requestUser -> Optional.ofNullable(ServiceRequestUserDTO.builder()
                    .name(requestUser.getName())
                    .surname(requestUser.getSurname())
                    .age(requestUser.getAge())
                    .cpf(requestUser.getCpf())
                    .login(requestUser.getLogin())
                    .password(requestUser.getPassword())
                    .build())
            .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST,"Invalid Data"));

    public static final Function<ServiceResponseUserDTO, ControllerResponseUserDTO> toControllerResponseUserDTO =
            responseUser -> Optional.ofNullable(ControllerResponseUserDTO.builder()
                    .id(responseUser.getId())
                    .name(responseUser.getName())
                    .surname(responseUser.getSurname())
                    .age(responseUser.getAge())
                    .cpf(responseUser.getCpf())
                    .admin(responseUser.isAdmin())
                    .build())
            .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST,"Invalid Data"));

    public static final Function<ServiceRequestUserDTO, User> toUserSave =
            request -> Optional.ofNullable(User.builder()
                    .name(request.getName())
                    .login(request.getLogin())
                    .password(request.getPassword())
                    .surname(request.getSurname())
                    .cpf(request.getCpf())
                    .age(request.getAge())
                    .build())
            .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST,"Invalid Data"));

    public static final Function<User, ServiceResponseUserDTO> toServiceResponseUserDTO =
            user -> Optional.ofNullable(ServiceResponseUserDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .age(user.getAge())
                    .cpf(user.getCpf())
                    .admin(user.isAdmin())
                    .build())
            .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST,"invalid Data"));

    public static final BiFunction<String, ServiceRequestUserDTO, User> toUserUpdate =
            (id, request) -> Optional.ofNullable(User.builder()
                    .id(id)
                    .name(request.getName())
                    .login(request.getLogin())
                    .password(request.getPassword())
                    .surname(request.getSurname())
                    .cpf(request.getCpf())
                    .age(request.getAge())
                    .build())
            .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST,"invalid Data"));
}
