package com.axisrooms.hcr.repository;

import com.axisrooms.hcr.dto.user.ERole;
import com.axisrooms.hcr.dto.user.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
  Role findByName(ERole name);
}
