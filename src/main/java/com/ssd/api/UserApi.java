package com.ssd.api;

import com.ssd.dal.adapter.UserAdapterMongoImpl;
import com.ssd.dal.model.User;
import com.ssd.dto.UserLoginDto;
import com.ssd.dto.UserRegisterDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserApi {

    private final UserAdapterMongoImpl userAdapterMongo;

    @Autowired
    public UserApi(UserAdapterMongoImpl userAdapterMongo) {
        this.userAdapterMongo = userAdapterMongo;
    }

    public ResponseEntity<?> register(UserRegisterDto userRegister) {
        return userAdapterMongo.registerUser(userRegister);
    }

    public ResponseEntity<?> login(UserLoginDto userLogin) {
        return userAdapterMongo.userLogin(userLogin);

    }

    public List<User> getAll() {
        return userAdapterMongo.getAllUsers();
    }
}