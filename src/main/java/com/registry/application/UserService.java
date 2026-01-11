package com.registry.application;

import com.registry.domain.User;
import com.registry.infra.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserEventGateway gateway;

    public UserService(UserRepository repository, UserEventGateway gateway) {
        this.repository = repository;
        this.gateway = gateway;
    }

    @Transactional
    public User create(User u) {

        User userCreated = repository.save(u);

        gateway.sendUserToUpdateStatus(userCreated);

        return userCreated;
    }

    public List<User> list() {
        return repository.findAll();
    }

}
