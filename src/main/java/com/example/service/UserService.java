package com.example.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.model.User;

public interface UserService {

	User saveOrUpdate(User entity);

	User findById(String id);

	User findByEmail(String email);

	void deleteById(String id);

	Page<User> findAllByFullNameLike(String userpagekeyword, Pageable pageable);
	
	User checkLogin(String email, String password);

}
