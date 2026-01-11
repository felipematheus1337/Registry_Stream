package com.registry.infra;

import com.registry.application.UserEventGateway;
import com.registry.domain.User;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.time.Instant;
import java.util.UUID;

@Component
public class UserEventGatewayImpl implements UserEventGateway {

    private final StreamBridge streamBridge;

    public UserEventGatewayImpl(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void sendUserToUpdateStatus(User u) {

        if (u == null) throw new RuntimeException("Nullable User not allowed in payload.");

        UUID eventId = UUID.randomUUID();

        Message<User> message = MessageBuilder
                .withPayload(u)
                .setHeader("eventId", eventId.toString())
                .setHeader("eventType", "USER_CREATED")
                .setHeader("ocurredAt", Instant.now().toString())
                .setHeader("aggregatedId", String.valueOf(u.getId()))
                .build();

        streamBridge.send("userCreated-out-0", message);

    }
}
