package com.papers.paperspapeleria.repository;

import com.papers.paperspapeleria.entity.User;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByIdentification(String identificacion);

    List<User> findByRols_Name(String roleName);
}