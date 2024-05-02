package com.file.uploader.repository;

import com.file.uploader.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // JPA for basic CRUD operations with entities

    Optional<User> findById(Long id);
    Optional<User> findByUsername (String username);

}
