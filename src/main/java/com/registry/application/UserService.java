package com.registry.application;

import com.registry.domain.User;
import com.registry.infra.UserRepository;
import com.registry.infra.persistence.UserProcessedEvent;
import com.registry.infra.persistence.UserProcessedEventRepository;
import com.registry.infra.persistence.enums.ProcessStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserProcessedEventRepository userProcessedEventRepository;
    private final UserEventGateway gateway;

    public UserService(UserRepository repository, UserProcessedEventRepository userProcessedEventRepository, UserEventGateway gateway) {
        this.repository = repository;
        this.userProcessedEventRepository = userProcessedEventRepository;
        this.gateway = gateway;
    }

    @Transactional
    public User create(User u) {

        User userCreated = repository.save(u);

        UserProcessedEvent userProcessedEvent = new UserProcessedEvent();
        userProcessedEvent.setUserId(userCreated.getId());
        userProcessedEvent.setStatus(ProcessStatus.PENDING);

        userProcessedEventRepository.save(userProcessedEvent);

        gateway.sendUserToUpdateStatus(userCreated);

        return userCreated;
    }

    public List<User> list() {
        return repository.findAll();
    }

}
