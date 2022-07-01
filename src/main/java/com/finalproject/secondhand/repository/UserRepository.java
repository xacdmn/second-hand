package com.finalproject.secondhand.repository;

import com.finalproject.secondhand.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<Users, Integer> {

    Users findByUsername(String username);
    Users findUsersByUserId(Integer userId);
    Users findUsersByUsername(String username);
    Users findUsersByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);


}

