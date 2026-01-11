package com.registry.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private TypeUserStatus status = TypeUserStatus.MODERATOR;


    public User() {
    }

    public User(Long id, String name, Integer age, TypeUserStatus status) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Nullable
    public TypeUserStatus getStatus() {
        return status;
    }

    public void setStatus(@Nullable TypeUserStatus status) {
        this.status = status;
    }
}
