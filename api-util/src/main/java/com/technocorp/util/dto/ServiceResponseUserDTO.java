package com.technocorp.util.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceResponseUserDTO {

    private String id;
    private String name;
    private String surname;
    private String age;
    private String cpf;
    private boolean admin;


}
