package com.nuvola.myproject.server.security.corp;


import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.OAuth2Operations;

public class CorporateConnectionFactory extends OAuth2ConnectionFactory<Corporate> {
    public CorporateConnectionFactory(OAuth2Operations oAuth2Operations) {
        super("corporate", new CorporateServiceProvider(oAuth2Operations), new CorporateAdapter());
    }
}
