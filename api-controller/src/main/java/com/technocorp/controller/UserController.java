package com.technocorp.controller;


import com.technocorp.controller.dto.ControllerRequestUserDTO;
import com.technocorp.controller.dto.ControllerResponseUserDTO;
import com.technocorp.controller.dto.MapperDTO;
import com.technocorp.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController

@AllArgsConstructor
@RequestMapping("/users")
@Api("User Resource")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(OK)
    @ApiOperation("List all user resources")
    public List<ControllerResponseUserDTO> listAllUsers() {
        var serviceResponse = userService.listAllUsers();
        return serviceResponse.stream()
                .map(MapperDTO::toControllerResponseUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{name}")
    @ResponseStatus(OK)
    @ApiOperation("Find an user resource by name")
    public List<ControllerResponseUserDTO> findByName(@PathVariable String name) {
        var serviceResponse = userService.findByName(name);
        return serviceResponse.stream()
                .map(MapperDTO::toControllerResponseUserDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("save an user resource")
    public ControllerResponseUserDTO save(@RequestBody ControllerRequestUserDTO requestUserDTO) {
        var response = userService
                .save(MapperDTO.toServiceRequestUserDTO(requestUserDTO));
        return MapperDTO.toControllerResponseUserDTO(response);
    }

    @PutMapping
    @ResponseStatus(OK)
    @ApiOperation("Update an user resource ")
    public ControllerResponseUserDTO update(@RequestBody ControllerRequestUserDTO requestUserDTO) {
        var response = userService
                .save(MapperDTO.toServiceRequestUserDTO(requestUserDTO));
        return MapperDTO.toControllerResponseUserDTO(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Delete an user resource")
    public void deleteById(@PathVariable String id) {
        userService.deleteById(id);
    }

}
