package com.technocorp.repository;

import com.technocorp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

    List<User> findByNameIgnoreCase(String name);

}
