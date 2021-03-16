package com.technocorp.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("ControllerRequestUserDTO : The request user resource of the Rest Api")
public class ControllerRequestUserDTO {

    @ApiModelProperty(value = "Name of the user")
    private String name;
    @ApiModelProperty(value = "Surname of the user")
    private String surname;
    @ApiModelProperty(value = "Age of the user")
    private String age;
    @ApiModelProperty(value = "CPF of the user")
    private String cpf;
    @ApiModelProperty(value = "Login of the user(Not returned)")
    private String login;
    @ApiModelProperty(value = "Password of the user(Not Returned)")
    private String password;

}
