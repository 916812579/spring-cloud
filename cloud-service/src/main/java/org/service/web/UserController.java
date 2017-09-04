package org.service.web;

import java.util.List;

import org.service.domain.UserService;
import org.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public List<User> readUserInfo() {
		List<User> ls = userService.searchAll();
		return ls;
	}
}
