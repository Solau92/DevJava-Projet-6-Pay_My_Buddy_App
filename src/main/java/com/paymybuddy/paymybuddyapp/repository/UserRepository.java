package com.paymybuddy.paymybuddyapp.repository;

import com.paymybuddy.paymybuddyapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
}
