package com.registry.infra;

import com.registry.application.UserEventGateway;
import com.registry.domain.User;
import com.registry.infra.persistence.UserProcessedEventRepository;
import com.registry.infra.persistence.enums.ProcessStatus;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.time.Instant;
import java.util.UUID;

@Component
public class UserEventGatewayImpl implements UserEventGateway {

    private final StreamBridge streamBridge;
    private final UserProcessedEventRepository repository;

    public UserEventGatewayImpl(StreamBridge streamBridge, UserProcessedEventRepository repository) {
        this.streamBridge = streamBridge;
        this.repository = repository;
    }

    @Override
    public void sendUserToUpdateStatus(User u) {

        if (u == null) throw new RuntimeException("Nullable User not allowed in payload.");

        String eventId = UUID.randomUUID().toString();

        if (eventId.isBlank()) throw new RuntimeException("eventId can't be nullable.");

        var processedUserEvent = repository.findByUserId(u.getId());

        if ( processedUserEvent.isEmpty() ) {
            throw new RuntimeException("FAILED - User not traced.");
        }

        var userEvent = processedUserEvent.get();

        userEvent
                .setStatus(ProcessStatus.PUBLISHED);

        userEvent.setEventId(eventId);

        repository.save(userEvent);

        Message<User> message = MessageBuilder
                .withPayload(u)
                .setHeader("eventId", eventId)
                .setHeader("eventType", "USER_CREATED")
                .setHeader("ocurredAt", Instant.now().toString())
                .setHeader("aggregatedId", String.valueOf(u.getId()))
                .build();

        streamBridge.send("userCreated-out-0", message);

    }
}
