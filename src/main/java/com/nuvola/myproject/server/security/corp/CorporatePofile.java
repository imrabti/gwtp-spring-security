package com.nuvola.myproject.server.security.corp;

public class CorporatePofile {
    private String id;
    private String email;
    private String username;
    private String name;
    private String firstName;
    private String lastName;

    public CorporatePofile() {
    }

    public CorporatePofile(String id, String email, String username, String name, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
