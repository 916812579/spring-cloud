package org.service.dao;

import java.util.List;

import org.service.model.User;

public interface UserMapper {
	
	List<User> findAll();
	
}
