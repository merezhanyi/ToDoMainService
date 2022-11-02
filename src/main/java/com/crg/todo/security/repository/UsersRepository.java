package com.crg.todo.security.repository;

import com.crg.todo.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {
}