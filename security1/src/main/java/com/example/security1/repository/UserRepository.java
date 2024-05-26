package com.example.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.security1.model.User;


// @Repository 어노테이션이 없어도 IoC 가능 -> JpaRepository 를 상속했기 때문에
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// findBy규칙 -> Username 문법
	// select * from user where username = ?
	public User findByUsername(String username); // Jpa Query methods

}
