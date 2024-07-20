package com.compulynxtest.compulynxtest.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);
    Optional<User> findByIdno(String idno);
    boolean existsByEmail(String email);
    boolean existsByIdno(String idno);
}
