package com.registry.infra.controller;

import com.registry.application.UserService;
import com.registry.domain.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }


    @PostMapping
    ResponseEntity<User> createUser(@Valid @RequestBody User user) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user));

    }

    @GetMapping
    ResponseEntity<List<User>> list() {

        return ResponseEntity.ok(service.list());

    }


}
