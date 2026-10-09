package org.springSec.auth.infrastructure.persistence.repository;

import java.util.UUID;
import java.util.Optional;

import org.springSec.auth.infrastructure.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, UUID>{
    Optional<User> findByUsername(String username);
}
