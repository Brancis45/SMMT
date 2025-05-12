package com.monitoring.smmt.userlogin;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

//userrep for spring security
//used in userdetailservice
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

