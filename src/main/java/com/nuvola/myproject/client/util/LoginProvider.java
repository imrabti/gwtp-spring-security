package com.nuvola.myproject.client.util;

public enum LoginProvider {
    CORPORATE("corporate");

    private final String name;

    LoginProvider(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
