package com.shegami.usermanagementservice.entities;

import com.shegami.usermanagementservice.models.Permission;
import com.shegami.usermanagementservice.models.Type;
import jakarta.persistence.*;


@Entity
@Table(name = "permissions")
public class AccountPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private Permission name;

}
