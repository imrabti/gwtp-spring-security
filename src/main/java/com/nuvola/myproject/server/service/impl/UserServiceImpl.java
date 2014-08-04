package com.nuvola.myproject.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nuvola.myproject.server.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final static String USER_TEST = "root\";";

    private final LoggedInChecker loggedInChecker;

    @Autowired
    UserServiceImpl(UserRepository userRepository,
                    LoggedInChecker loggedInChecker) {
        this.userRepository = userRepository;
        this.loggedInChecker = loggedInChecker;
    }

    @Override
    public User getUserByUsername(String username) {
        if (username.equals(USER_TEST)) {
            User user = new User();
            user.setLogin(USER_TEST);
            user.setPassword(new ShaPasswordEncoder().encodePassword("password", null));

            return user;
        } else {
            return userRepository.findByLogin(username);
        }
    }

    @Override
    public List<String> getPermissions(Long userId) {
        return new ArrayList<>();
    }

    @Override
    public Boolean isCurrentUserLoggedIn() {
        return loggedInChecker.getLoggedInUser() != null;
    }
}
