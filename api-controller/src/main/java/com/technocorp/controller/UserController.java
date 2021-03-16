package com.technocorp.controller;


import com.technocorp.controller.dto.ControllerRequestUserDTO;
import com.technocorp.controller.dto.ControllerResponseUserDTO;
import com.technocorp.controller.dto.MapperDTO;
import com.technocorp.service.UserServiceImpl;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Api("User Resource")
@CrossOrigin("http://localhost")
public class UserControllerImpl {

    private final UserServiceImpl userServiceImpl;

    @GetMapping
    @ResponseStatus(OK)
    @ApiOperation("List all user resources")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Succefully retrieved list"),
            @ApiResponse(code = 401, message = "Not Authorized to view this resource"),
            @ApiResponse(code = 403, message = "Forbiddden to acess this resource"),
            @ApiResponse(code = 404, message = "The resource you requested was not found"),
            @ApiResponse(code = 204, message = "The resource not exist")
    })
    public List<ControllerResponseUserDTO> listAllUsers() {
        return userServiceImpl.findAll().stream()
                .map(MapperDTO::toControllerResponseUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{name}")
    @ResponseStatus(OK)
    @ApiOperation("Find an user resource by name")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Succefully retrieved list"),
            @ApiResponse(code = 401, message = "Not Authorized to view this resource"),
            @ApiResponse(code = 403, message = "Forbiddden to acess this resource"),
            @ApiResponse(code = 404, message = "The resource you requested was not found"),
            @ApiResponse(code = 204, message = "The resource not exist")
    })
    public List<ControllerResponseUserDTO> findByName(@PathVariable String name) {
        return userServiceImpl.findByName(name).stream()
                .map(MapperDTO::toControllerResponseUserDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("save an user resource")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 403, message = "Forbidden to access this resource"),
            @ApiResponse(code = 201, message = "Resource created with success")

    })
    public ControllerResponseUserDTO save(@RequestBody ControllerRequestUserDTO requestUserDTO) {
        return MapperDTO.toControllerResponseUserDTO(
                userServiceImpl.save(MapperDTO.toServiceRequestUserDTO(requestUserDTO)));
    }

    @PutMapping
    @ResponseStatus(OK)
    @ApiOperation("Update an user resource ")
    public ControllerResponseUserDTO update(@ApiParam(value = "the id to update the user", required = true)
                                            @RequestParam(required = true) String id,
                                            @RequestBody ControllerRequestUserDTO requestUserDTO) {
        return MapperDTO.toControllerResponseUserDTO(userServiceImpl
                .update(id, MapperDTO.toServiceRequestUserDTO(requestUserDTO)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Delete an user resource")
    @ApiResponses(value = {@ApiResponse(code = 401, message = "Not Authorized to view this resource"),
            @ApiResponse(code = 404, message = "The resource you requested was not found"),
            @ApiResponse(code = 204, message = "The resource not exist")
    })
    public void deleteById(@PathVariable String id) {
        userServiceImpl.deleteById(id);
    }

}
