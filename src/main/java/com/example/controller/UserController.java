package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.converter.BytesToUserConverter;
import com.example.converter.UserToBytesConverter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Service
public class UserController {
	
    private RedisTemplate redisTemplate;
    private BytesToUserConverter bytesToUserConverter;
    private UserToBytesConverter userToBytesConverter;

    @Autowired
	public UserController(RedisTemplate redisTemplate, BytesToUserConverter bytesToUserConverter, UserToBytesConverter userToBytesConverter) {
    	this.redisTemplate = redisTemplate;
    	this.bytesToUserConverter = bytesToUserConverter;
    	this.userToBytesConverter = userToBytesConverter;
		User user = new User("1234");
		System.out.println(redisTemplate);
		HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("user", user.getPhoneNumber(), userToBytesConverter.convert(user));
	}

    @RequestMapping(value = "/user/{phoneNumber}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public User findUserByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
    	HashOperations hashOperations = redisTemplate.opsForHash();
        return bytesToUserConverter.convert((byte[]) hashOperations.get("user", phoneNumber));
    }

}
