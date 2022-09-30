package com.ssd.dal.adapter;

import com.ssd.dal.model.User;
import com.ssd.dal.model.UserDetailsModel;
import com.ssd.dal.repo.UserMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserMongoRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found!"));

        return UserDetailsModel.build(user);
    }

    public List<User> getAllUserDetails() {
        return new ArrayList<>(userRepository.findAll());
    }
}
