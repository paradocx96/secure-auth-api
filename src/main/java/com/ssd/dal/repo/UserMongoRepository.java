package com.ssd.dal.repo;

import com.ssd.dal.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserMongoRepository extends MongoRepository<User, String> {

    Boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Boolean existsByEmail(String email);

}
