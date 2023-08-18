package com.axisrooms.hcr.repository;

import com.axisrooms.hcr.dto.user.LoginActivity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginRepository extends MongoRepository<LoginActivity, String> {
    List<LoginActivity> findFirst10ByEmailOrderByLastLoginDesc(String email);
}