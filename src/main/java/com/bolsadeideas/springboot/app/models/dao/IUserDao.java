package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.User;

public interface IUserDao extends CrudRepository<User, Long> {

	public User findByUsername(String username);
	
}
