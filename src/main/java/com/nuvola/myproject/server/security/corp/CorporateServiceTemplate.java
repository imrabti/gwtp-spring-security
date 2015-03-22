package com.nuvola.myproject.server.security.corp;

import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.web.client.HttpClientErrorException;

public class CorporateServiceTemplate extends AbstractOAuth2ApiBinding implements Corporate {
    private final static String corporateProfileURL = "http://localhost:8080/oauthprotoserver/profile/";
    private String accessToken;

    CorporateServiceTemplate(String accessToken) {
        super(accessToken);
        this.accessToken = accessToken;
    }

    @Override
    public CorporatePofile getUserProfile() {
        try {
            ResponseEntity<String> content = getRestTemplate().exchange(URI.create(corporateProfileURL + accessToken),
                    HttpMethod.GET, null, String.class);

            // TODO Retrieve a Corporate JSON user build a serialized object
            return new CorporatePofile(content.getBody(),"test@test.com","client1","employee","foo","bar");

        }  catch (HttpClientErrorException e2) {
            throw new OAuth2Exception(e2.getMessage());
        }
    }

    @Override
    public void updateStatus(String message) {
      return;
    }
}
