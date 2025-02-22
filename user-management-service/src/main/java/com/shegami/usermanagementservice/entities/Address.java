package com.shegami.usermanagementservice.entities;


import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String street;

    private String city;
    private String state;
    private String zip;
    private String country;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

}
