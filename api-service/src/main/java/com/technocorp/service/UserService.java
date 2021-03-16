package com.technocorp.service;

import com.technocorp.util.dto.ServiceRequestUserDTO;
import com.technocorp.util.dto.ServiceResponseUserDTO;

import java.util.List;

public interface UserService {

    List<ServiceResponseUserDTO> findAll();
    List<ServiceResponseUserDTO> findByName(String name);
    ServiceResponseUserDTO save(ServiceRequestUserDTO requestDTO);
    ServiceResponseUserDTO update(String id,ServiceRequestUserDTO requestDTO);
    void deleteById(String id);

}
