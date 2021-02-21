package com.technocorp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("users")
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
