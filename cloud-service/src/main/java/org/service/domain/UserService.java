package org.service.domain;

import java.util.List;

import org.service.dao.UserMapper;
import org.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

	
	@Autowired
	private UserMapper userMapper;
	
	public List<User> searchAll(){
		List<User> list = userMapper.findAll();
		return list;
	}
}