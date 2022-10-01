package com.ssd.dal.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ssd.dal.model.User;

public interface UserMongoRepository extends MongoRepository<User, String> {

    Boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    Boolean existsByEmail(String email);

}
