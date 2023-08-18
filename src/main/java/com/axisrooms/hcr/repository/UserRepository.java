package com.axisrooms.hcr.repository;

import com.axisrooms.hcr.dto.user.Role;
import com.axisrooms.hcr.dto.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    Optional<User> findByUsername(String username);

    List<User> findByActiveAndRoles(boolean active, Set<Role> roles);

    User findBySuppliersIn(Set<Long> ids);

}