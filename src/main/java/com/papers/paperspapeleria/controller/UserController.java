package com.papers.paperspapeleria.controller;

import java.util.List;

import com.papers.paperspapeleria.dto.UserDTO;
import com.papers.paperspapeleria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO crearTercero(@RequestBody UserDTO userDTO) {
        
        return userService.createUser(userDTO);
    }

    @GetMapping
    public List<UserDTO> listarUsers() {
        return userService.listUsers();
    }

    @GetMapping("/{id}") 
    public UserDTO getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserDTO upDaterUser(
            @PathVariable String id,
            @RequestBody UserDTO userDTO
    ) {
        return userService.upDateUser(id, userDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void eliminarUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}