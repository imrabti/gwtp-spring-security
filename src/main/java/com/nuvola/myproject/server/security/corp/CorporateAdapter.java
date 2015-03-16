package com.nuvola.myproject.server.security.corp;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;

public class CorporateAdapter implements ApiAdapter<Corporate> {
    public boolean test(Corporate corporate) {
        try {
            corporate.getUserProfile();
            return true;
        } catch (ApiException e) {
            return false;
        }
    }

    public void setConnectionValues(Corporate corporate, ConnectionValues values) {
        CorporatePofile profile = corporate.getUserProfile();
        values.setProviderUserId(profile.getId());
        values.setDisplayName(profile.getUsername());
        values.setProfileUrl("http://localhost:8080/oauthprotoserver/profile/" + profile.getId());
        //values.setImageUrl("http://localhost:8080/oauthprotoserver/profile/" + profile.getId() + "/picture");
    }

    public UserProfile fetchUserProfile(Corporate corporate) {
        CorporatePofile profile = corporate.getUserProfile();
        return new UserProfileBuilder().setName(profile.getName())
                .setFirstName(profile.getFirstName())
                .setLastName(profile.getLastName()).
                setEmail(profile.getEmail()).setUsername(profile.getUsername()).build();
    }

    public void updateStatus(Corporate corporate, String message) {
        corporate.updateStatus(message);
    }
}
