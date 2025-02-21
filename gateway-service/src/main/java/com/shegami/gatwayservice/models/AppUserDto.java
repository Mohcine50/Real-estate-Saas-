package com.shegami.gatwayservice.models;




import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;


public class AppUserDto {

    private String id;
    private String username;
    private String password;
    private String email;
    private Collection<RoleDto> roles = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Collection<RoleDto> getRoles() {
        return roles;
    }


    private AppUserDto(String id, String username, String password, String email, Collection<RoleDto> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public static class Builder {
        private String id;
        private String username;
        private String password;
        private String email;
        private Collection<RoleDto> roles = new ArrayList<>();

        public Builder id(String id) {
            this.id = id;
            return this;
        }
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder roles(Collection<RoleDto> roles) {
            this.roles = roles;
            return this;
        }
        public AppUserDto build() {
            return new AppUserDto(id, username, password, email, roles);
        }
    }

}
