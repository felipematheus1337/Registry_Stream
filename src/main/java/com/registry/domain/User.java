package com.registry.domain;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private TypeUserStatus status;


    public User() {
    }

    public User(Long id, String name, Integer age, TypeUserStatus status) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.status = status;
    }
}
