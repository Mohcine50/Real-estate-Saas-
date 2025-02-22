package com.shegami.usermanagementservice.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String firstName;

    private String lastName;

    @OneToOne(fetch = FetchType.LAZY)
    private Account account;


    @OneToMany(mappedBy = "profile", fetch = FetchType.EAGER)
    private Collection<Address> addresses = new ArrayList<>();

}
