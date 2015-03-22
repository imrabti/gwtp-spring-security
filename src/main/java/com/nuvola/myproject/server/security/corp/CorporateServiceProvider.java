package com.nuvola.myproject.server.security.corp;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Operations;

public class CorporateServiceProvider extends AbstractOAuth2ServiceProvider<Corporate> {
    public CorporateServiceProvider(OAuth2Operations oauth2Operations) {
        super(oauth2Operations);
    }

    @Override
    public Corporate getApi(String s) {
        return new CorporateServiceTemplate(s);
    }
}
