package com.shegami.gatwayservice.models;


public class AccountTypeDto {

    private String id;
    private String name;

    private AccountTypeDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public static class Builder {
        private String id;
        private String name;

        public AccountTypeDto build() {
            return new AccountTypeDto(id, name);
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

    }
}
