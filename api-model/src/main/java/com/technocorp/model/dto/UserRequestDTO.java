package com.technocorp.model.dto;

import com.technocorp.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    @Id
    private String name;
    private String surname;
    private String age;
    private String cpf;
    private String login;
    private String password;

    public User build(){
        return User.builder()
                .name(this.name)
                .surname(this.surname)
                .age(this.age)
                .cpf(this.cpf)
                .login(this.login)
                .password(this.password)
                .build();
    }

}
