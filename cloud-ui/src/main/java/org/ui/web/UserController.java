package org.ui.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ui.model.User;
import org.ui.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "/users")
	public ResponseEntity<List<User>> readUserInfo() {
		List<User> users = userService.readUserInfo();
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
}