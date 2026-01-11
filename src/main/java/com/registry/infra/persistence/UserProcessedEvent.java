package com.registry.infra.persistence;

import com.registry.infra.persistence.enums.ProcessStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "user_processed_event")
public class UserProcessedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @Enumerated(EnumType.STRING)
    private ProcessStatus status;

    private String eventId;

    public UserProcessedEvent() {
    }

    public UserProcessedEvent(Long id, Long userId, ProcessStatus status, String eventId) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.eventId = eventId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
