package com.example.todoManager.repository;

import com.example.todoManager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, String> , JpaSpecificationExecutor<User> {

}