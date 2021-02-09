package com.technocorp.model.dto;

import com.technocorp.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String name;
    private String surname;
    private String age;
    private String cpf;
    private boolean admin;

    public UserResponseDTO(User user) {
        this.name = user.getName();
        this.surname = user.getSurname();
        this.age = user.getAge();
        this.cpf = user.getCpf();
        this.admin = user.isAdmin();
    }

}
