package com.nuvola.myproject.server.security.corp;

public interface Corporate {
    CorporatePofile getUserProfile();

    void updateStatus(String message);
}
