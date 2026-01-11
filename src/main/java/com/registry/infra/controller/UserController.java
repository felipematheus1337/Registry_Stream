package com.registry.infra.controller;

import com.registry.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
public class UserController {


    @PostMapping
    ResponseEntity<?> createUser(@RequestBody User user) {

    }

    @GetMapping
    ResponseEntity<?> list() {

    }


}
