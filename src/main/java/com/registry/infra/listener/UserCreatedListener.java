package com.registry.infra.listener;

import com.registry.domain.TypeUserStatus;
import com.registry.domain.User;
import com.registry.infra.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class UserCreatedListener implements Consumer<Message<User>> {

    private final UserRepository repository;

    public UserCreatedListener(UserRepository repository) {
        this.repository = repository;
    }


    @Override
    public void accept(Message<User> userMessage) {
        User user = userMessage.getPayload();
        if ( user == null ) throw new RuntimeException("Error ao serializar.");
        user.setStatus(TypeUserStatus.ADMINISTRATOR);
        repository.save(user);
    }
}
