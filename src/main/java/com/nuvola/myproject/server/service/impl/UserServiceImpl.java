package com.nuvola.myproject.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nuvola.myproject.server.security.LoggedInChecker;
import com.nuvola.myproject.server.service.UserService;
import com.nuvola.myproject.shared.model.User;

@Service
public class UserServiceImpl implements UserService {
    private final static String USER_TEST = "root";
    private final static String USER_CORPORATE_TEST = "client1";

    private final LoggedInChecker loggedInChecker;

    @Autowired
    UserServiceImpl(LoggedInChecker loggedInChecker) {
        this.loggedInChecker = loggedInChecker;
    }

    @Override
    public User getCorporateUser(String username) {
        // Just a mock interface support only one single root
        // Here you can add DAO to load user from the database
        if (username.equals(USER_CORPORATE_TEST)) {
            User user = new User();
            user.setLogin(USER_CORPORATE_TEST);
            user.setPassword(new ShaPasswordEncoder().encodePassword("password", null));

            return user;
        } else {
            return null;
        }
    }

    @Override
    public User getLocalUserByUsername(String username) {
        // Just a mock interface support only one single root
        // Here you can add DAO to load user from the database
        if (username.equals(USER_TEST)) {
            User user = new User();
            user.setLogin(USER_TEST);
            user.setPassword(new ShaPasswordEncoder().encodePassword("password", null));

            return user;
        } else {
            return null;
        }
    }

    @Override
    public List<String> getPermissions(String username) {
        return new ArrayList<>();
    }

    @Override
    public Boolean isCurrentUserLoggedIn() {
        return loggedInChecker.getLoggedInUser() != null;
    }
}
