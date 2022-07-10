package com.com.security1.repository;

import com.com.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// @Repository 어노테이션이 없어도 IoC 된다. JpaRepository를 상속했기 때문.
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
