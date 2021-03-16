package com.technocorp.service.servicedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequestUserDTO {

    private String id;
    private String name;
    private String surname;
    private String age;
    private String cpf;
    private String login;
    private String password;



}
