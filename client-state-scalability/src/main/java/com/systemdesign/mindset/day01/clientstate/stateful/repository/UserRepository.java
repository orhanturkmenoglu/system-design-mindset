package com.systemdesign.mindset.day01.clientstate.stateful.repository;

import com.systemdesign.mindset.day01.clientstate.stateful.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Username ile kullanıcı bul
     */
    Optional<User> findByUsername(String username);

    /**
     * Username var mı kontrol et
     */
    boolean existsByUsername(String username);
}
