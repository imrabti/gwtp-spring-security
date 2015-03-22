package com.nuvola.myproject.server.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import com.nuvola.myproject.server.service.UserService;
import com.nuvola.myproject.shared.model.User;

@Service
public class NuvolaCasDetailsService implements SocialUserDetailsService {
    private final UserService userService;

    @Autowired
    NuvolaCasDetailsService(UserService userService) {
        this.userService = userService;
    }

    public SocialUserDetails loadUserByUserId(String username) throws UsernameNotFoundException {
        User user = userService.getCorporateUser(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<String> permissions = userService.getPermissions(user.getLogin());
        for (String permission : permissions) {
            grantedAuthorities.add(new SimpleGrantedAuthority(permission));
        }

        return new NuvolaCasUserDetails(user, grantedAuthorities);
    }


}
