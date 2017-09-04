package org.ui.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.ui.model.User;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class UserService {
	@Autowired
	RestTemplate restTemplate;

	final String SERVICE_NAME = "cloud-service";

	@HystrixCommand(fallbackMethod = "fallbackSearchAll")
	public List<User> readUserInfo() {
		// 通过应用名称访问服务
		return restTemplate.getForObject("http://" + SERVICE_NAME + "/user", List.class);
	}

	private List<User> fallbackSearchAll() {
		System.out.println("HystrixCommand fallbackMethod handle!");
		List<User> ls = new ArrayList<User>();
		User user = new User();
		user.setUsername("TestHystrixCommand");
		ls.add(user);
		return ls;
	}
}