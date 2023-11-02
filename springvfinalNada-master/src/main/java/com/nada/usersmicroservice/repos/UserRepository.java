package com.nada.usersmicroservice.repos;

import com.nada.usersmicroservice.entities.Role;
import com.nada.usersmicroservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("select u from User u where u.user_id = ?1")
    User getUserById(Long id);


}
