package com.shegami.usermanagementservice.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.shegami.usermanagementservice.models.Permission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AccountType> types = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AccountPermission> permissions = new ArrayList<>();



}