package com.registry.application;

import com.registry.domain.User;

public interface UserEventGateway {

    void sendUserToUpdateStatus(User u);
}
