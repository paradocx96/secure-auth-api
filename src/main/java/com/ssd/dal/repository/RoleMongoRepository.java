package com.ssd.dal.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ssd.dal.model.ERole;
import com.ssd.dal.model.Role;

public interface RoleMongoRepository extends MongoRepository<Role, String> {
	Optional<Role> findByName(ERole name);
	
}
