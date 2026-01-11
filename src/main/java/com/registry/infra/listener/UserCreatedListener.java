package com.registry.infra.listener;

import com.registry.domain.TypeUserStatus;
import com.registry.domain.User;
import com.registry.infra.UserRepository;
import com.registry.infra.persistence.UserProcessedEvent;
import com.registry.infra.persistence.UserProcessedEventRepository;
import com.registry.infra.persistence.enums.ProcessStatus;
import jakarta.transaction.Transactional;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Component
public class UserCreatedListener implements Consumer<Message<User>> {

    private final UserRepository repository;
    private final UserProcessedEventRepository userProcessedEventRepository;

    public UserCreatedListener(UserRepository repository,
                               UserProcessedEventRepository userProcessedEventRepository) {
        this.repository = repository;
        this.userProcessedEventRepository = userProcessedEventRepository;
    }

    @Override
    @Transactional
    public void accept(Message<User> userMessage) {
        User user = null;
        try {
            user = userMessage.getPayload();
            if (user == null) throw new RuntimeException("Payload User nulo / falha de desserialização.");

            var processedUserEventOpt = userProcessedEventRepository.findByUserId(user.getId());
            if (processedUserEventOpt.isEmpty()) {
                throw new RuntimeException("FAILED - User not traced.");
            }

            var userEvent = processedUserEventOpt.get();
            userEvent.setStatus(ProcessStatus.SUCCESS);
            userProcessedEventRepository.save(userEvent);

            user.setStatus(TypeUserStatus.ADMINISTRATOR);
            repository.save(user);

        } catch (Exception e) {
            // 1) eventId: tenta header, se não vier gera um fallback
            String eventIdFromHeader = userMessage.getHeaders().get("eventId", String.class);
            String eventId = (eventIdFromHeader != null && !eventIdFromHeader.isBlank())
                    ? eventIdFromHeader
                    : UUID.randomUUID().toString();

            // 2) userId: tenta do payload, se não der, tenta de header (aggregateId) como fallback
            Long userId = null;
            if (user != null && user.getId() != null) {
                userId = user.getId();
            } else {
                String aggregateId = userMessage.getHeaders().get("aggregateId", String.class);
                if (aggregateId != null && !aggregateId.isBlank()) {
                    try {
                        userId = Long.valueOf(aggregateId);
                    } catch (NumberFormatException ignore) {
                        // deixa null
                    }
                }
            }

            // 3) Busca registro de trace se tiver userId, senão cria um novo "órfão" (ainda assim rastreável)
            UserProcessedEvent upe = null;

            if (userId != null) {
                upe = userProcessedEventRepository.findByUserId(userId).orElse(null);
            }

            if (upe == null) {
                upe = new UserProcessedEvent();
                if (userId != null) {
                    upe.setUserId(userId);
                }
            }

            upe.setEventId(eventId);
            upe.setStatus(ProcessStatus.FAILED);

            // Se você quiser guardar info do erro, aqui é o lugar.
            // (ideal: adicionar coluna errorMessage / errorType / failedAt)
            // upe.setErrorMessage(truncate(e.getMessage(), 500));

            userProcessedEventRepository.save(upe);

            // 4) Repropaga para o binder aplicar retry e, se estourar, mandar pra DLQ
            throw e;
        }
    }
}
