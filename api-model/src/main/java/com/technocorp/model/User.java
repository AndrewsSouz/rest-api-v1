package com.technocorp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;
    private String name;
    private String surname;
    private String age;
    private String cpf;
    private String login;
    private String password;
    private boolean admin;
}
