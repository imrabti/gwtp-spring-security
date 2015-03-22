package com.nuvola.myproject.server.service;

import java.util.List;

import com.nuvola.myproject.shared.model.User;

public interface UserService {
    User getCorporateUser(String username);

    User getLocalUserByUsername(String username);

    List<String> getPermissions(String username);

    Boolean isCurrentUserLoggedIn();
}
