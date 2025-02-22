package com.shegami.gatwayservice.models;


import java.util.ArrayList;
import java.util.Collection;


public class AccountDto {

    private String id;
    private String password;
    private String email;
    private Collection<AccountTypeDto> types = new ArrayList<>();

    private AccountDto(String id, String password, String email, Collection<AccountTypeDto> roles) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.types = roles;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Collection<AccountTypeDto> getTypes() {
        return types;
    }

    public static class Builder {
        private String id;
        private String password;
        private String email;
        private Collection<AccountTypeDto> types = new ArrayList<>();

        public Builder id(String id) {
            this.id = id;
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

        public Builder types(Collection<AccountTypeDto> types) {
            this.types = types;
            return this;
        }

        public AccountDto build() {
            return new AccountDto(id, password, email, types);
        }
    }

}
