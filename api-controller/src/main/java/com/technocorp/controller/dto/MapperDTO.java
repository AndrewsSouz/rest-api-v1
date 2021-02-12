package com.technocorp.controller.dto;

import com.technocorp.service.servicedto.ServiceRequestUserDTO;
import com.technocorp.service.servicedto.ServiceResponseUserDTO;

public class MapperDTO {

    private MapperDTO(){}

    public static ServiceRequestUserDTO toServiceRequestUserDTO(
            ControllerRequestUserDTO requestUserDTO){

        return ServiceRequestUserDTO.builder()
                .id(requestUserDTO.getId())
                .name(requestUserDTO.getName())
                .surname(requestUserDTO.getSurname())
                .age(requestUserDTO.getAge())
                .cpf(requestUserDTO.getCpf())
                .login(requestUserDTO.getLogin())
                .password(requestUserDTO.getPassword())
                .build();
    }

    public static ControllerResponseUserDTO toControllerResponseUserDTO(
            ServiceResponseUserDTO serviceResponseUserDTO){

        return ControllerResponseUserDTO.builder()
                .id(serviceResponseUserDTO.getId())
                .name(serviceResponseUserDTO.getName())
                .surname(serviceResponseUserDTO.getSurname())
                .age(serviceResponseUserDTO.getAge())
                .cpf(serviceResponseUserDTO.getCpf())
                .admin(serviceResponseUserDTO.isAdmin())
                .build();
    }

}
