package com.ssd.dal.repo;

import com.ssd.dal.model.ERole;
import com.ssd.dal.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleMongoRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(ERole name);
}
