package com.papers.paperspapeleria.service;

import java.util.List;

import com.papers.paperspapeleria.dto.UserDTO;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> listUsers();

    UserDTO getUserById(String id);
}
