package com.ssd.controller;

import com.ssd.api.UserApi;
import com.ssd.dal.model.User;
import com.ssd.dto.UserLoginDto;
import com.ssd.dto.UserRegisterDto;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*",exposedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
public class UserEndpoint {

    UserApi userApi;

    @Autowired
    public UserEndpoint(UserApi userApi) {
        this.userApi = userApi;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> userRegister(@Valid @RequestBody UserRegisterDto userRegister) throws MessagingException, UnsupportedEncodingException {
        return userApi.registerUser(userRegister);
    }


    @PostMapping("/sign-in")
    public ResponseEntity<?> userLogin(@Valid @RequestBody UserLoginDto userLogin){
        return userApi.authUserLogin(userLogin);
    }

    @GetMapping("/get-all-users")
    public List<User> getAllUser(){
        return userApi.getAllUserDetails();
    }
}