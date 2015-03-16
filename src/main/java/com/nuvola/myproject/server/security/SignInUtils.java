package com.nuvola.myproject.server.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SignInUtils {
    /**
     * Programmatically signs in the user with the given the user ID.
     */
    public static Authentication signin(String userId) {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userId, null, null));
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
