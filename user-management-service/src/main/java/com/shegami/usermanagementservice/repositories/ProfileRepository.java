package com.shegami.usermanagementservice.repositories;

import com.shegami.usermanagementservice.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, String> {
}
