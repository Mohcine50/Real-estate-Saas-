package com.shegami.authservice.models;




import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;


@Getter
public class AccountDto {

    private String id;
    private String password;
    private String email;
    private Collection<AccountTypeDto> types = new ArrayList<>();


    private AccountDto(String id, String password, String email, Collection<AccountTypeDto> types) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.types = types;
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
