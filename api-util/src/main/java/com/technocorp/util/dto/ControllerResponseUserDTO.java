package com.technocorp.util.dto;

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
@ApiModel("ControllerResponseUserDTO : The response user resource of the Rest Api")
public class ControllerResponseUserDTO {

    @ApiModelProperty(value = "Identifier of the user")
    private String id;
    @ApiModelProperty(value = "Name of the user")
    private String name;
    @ApiModelProperty(value = "Surname of the user")
    private String surname;
    @ApiModelProperty(value = "Age of the user")
    private String age;
    @ApiModelProperty(value = "CPF of the user")
    private String cpf;
    @ApiModelProperty(value = "If the user is an admin")
    private boolean admin;
}
