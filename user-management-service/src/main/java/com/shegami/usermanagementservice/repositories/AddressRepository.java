package com.shegami.usermanagementservice.repositories;

import com.shegami.usermanagementservice.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
}
