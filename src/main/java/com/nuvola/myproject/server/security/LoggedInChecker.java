package com.nuvola.myproject.server.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nuvola.myproject.shared.model.User;

@Component
public class LoggedInChecker {
    public User getLoggedInUser() {
        User user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            // principal can be "anonymousUser" (String)
            if (principal instanceof NuvolaUserDetails) {
                // Local authenticated user
                NuvolaUserDetails userDetails = (NuvolaUserDetails) principal;
                user = userDetails.getUser();
            } else if (principal instanceof NuvolaCasUserDetails) {
                // Corporate authenticated user
                NuvolaCasUserDetails userDetails = (NuvolaCasUserDetails) principal;
                user = userDetails.getUser();
            }
        }

        return user;
    }
}
