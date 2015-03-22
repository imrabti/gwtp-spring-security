package com.nuvola.myproject.server.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.security.SocialUserDetails;

public class SignInUtils {
    public static Authentication signin(SocialUserDetails user) {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        user, user, user.getAuthorities()));
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
