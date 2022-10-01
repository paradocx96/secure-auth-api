package com.ssd.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssd.api.UserApi;
import com.ssd.dal.model.User;
import com.ssd.dto.UserLoginDto;
import com.ssd.dto.UserRegisterDto;


@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*", exposedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
public class UserEndpoint {

    @Autowired
    UserApi userApi;

    @PostMapping("/register")
    public ResponseEntity<?> userRegister(@Valid @RequestBody UserRegisterDto userRegister) {
        return userApi.register(userRegister);
    }


    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody UserLoginDto userLogin) {
        return userApi.login(userLogin);

    }

    @GetMapping("/users")
    public List<User> getAllUser() {
        return userApi.getAll();
    }
}
