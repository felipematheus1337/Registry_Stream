package com.registry.application;

import com.registry.domain.User;
import com.registry.infra.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public User create(User u) {

        return repository.save(u);
    }

    public List<User> list() {
        return repository.findAll();
    }

}
