package com.technocorp.service.servicedto;

import com.technocorp.model.User;

public class MapperDTO {

    private MapperDTO(){}

    public static ServiceResponseUserDTO toServiceResponseUserDTO(User user){
        return ServiceResponseUserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .cpf(user.getCpf())
                .admin(user.isAdmin())
                .build();
    }

    public static User toUserSave(ServiceRequestUserDTO requestUserDTO){
        return User.builder()
                .id(requestUserDTO.getId())
                .name(requestUserDTO.getName())
                .login(requestUserDTO.getLogin())
                .password(requestUserDTO.getPassword())
                .surname(requestUserDTO.getSurname())
                .cpf(requestUserDTO.getCpf())
                .age(requestUserDTO.getAge())
                .build();
    }

    public static User toUserUpdate(ServiceRequestUserDTO requestUserDTO){
        return User.builder()
                .id(requestUserDTO.getId())
                .name(requestUserDTO.getName())
                .login(requestUserDTO.getLogin())
                .password(requestUserDTO.getPassword())
                .surname(requestUserDTO.getSurname())
                .cpf(requestUserDTO.getCpf())
                .age(requestUserDTO.getAge())
                .build();
    }
}
