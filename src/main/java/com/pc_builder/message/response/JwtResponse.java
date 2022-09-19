package com.pc_builder.message.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtResponse {
    @JsonProperty("jwt_token")
    private String token;

    @JsonProperty("jwt_token_type")
    private String type = "Bearer";

    private Long id;
    private String username;
    private List<String> roles;
    private List<String> authorities;

    public JwtResponse(String token, Long id, String username, List<String> roles, List<String> authorities) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.authorities = authorities;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

}
