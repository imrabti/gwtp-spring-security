package com.nuvola.myproject.server.service;

import java.util.List;

import com.nuvola.core.shared.model.User;

public interface UserService {
    User getUserByUsername(String username);

    List<String> getPermissions(Long userId);

    Boolean isCurrentUserLoggedIn();
}
