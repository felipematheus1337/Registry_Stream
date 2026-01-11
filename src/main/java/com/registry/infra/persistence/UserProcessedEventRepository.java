package com.registry.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProcessedEventRepository extends JpaRepository<UserProcessedEvent, Long> {

    Optional<UserProcessedEvent> findByUserId(Long id);
}
