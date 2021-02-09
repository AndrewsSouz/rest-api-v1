package com.technocorp.repository;

import com.technocorp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {

    User findByNameIgnoreCase(String name);

}
