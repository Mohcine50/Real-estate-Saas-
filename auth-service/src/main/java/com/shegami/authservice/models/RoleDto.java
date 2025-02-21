package com.shegami.authservice.models;


public class RoleDto {

        private String id;
        private String name;

        private RoleDto(String id, String name) {
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
                public RoleDto build() {
                        return new RoleDto(id, name);
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
