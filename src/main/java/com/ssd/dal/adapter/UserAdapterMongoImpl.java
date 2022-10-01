package com.ssd.dal.adapter;

import com.ssd.dal.model.ERole;
import com.ssd.dal.model.Role;
import com.ssd.dal.model.User;
import com.ssd.dal.repository.RoleMongoRepository;
import com.ssd.dal.repository.UserMongoRepository;
import com.ssd.dto.JwtResponseDto;
import com.ssd.dto.MessageResponseDto;
import com.ssd.dto.UserLoginDto;
import com.ssd.dto.UserRegisterDto;
import com.ssd.security.jwt.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserAdapterMongoImpl {

    UserMongoRepository userRepository;
    RoleMongoRepository roleRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public UserAdapterMongoImpl(UserMongoRepository userRepository, RoleMongoRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    // Login User
    public ResponseEntity<?> userLogin(@Valid @RequestBody UserLoginDto userLoginDto) {

        // Get username and password and create new AuthenticationToken
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getUsername(),
                        userLoginDto.getPassword()
                )
        );

        // Set above assigned user credentials using Authentication object
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // After that create new JWT Token for that person
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Then get authentication principles and set that UserDetailimpl object
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Get getAuthorities and set to List object
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Return JWT response to FrontEnd
        return ResponseEntity.ok(new JwtResponseDto(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    // Register User
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDto userRegister) {

        if (userRepository.existsByUsername(userRegister.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseDto("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(userRegister.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseDto("Email is already taken!"));
        }

        // New User Object
        User user = new User(
                userRegister.getUsername(),
                userRegister.getContactNo(),
                passwordEncoder.encode(userRegister.getPassword()),
                userRegister.getEmail(),
                userRegister.getUserType());

        // Hashset for user Roles
        Set<Role> roles = new HashSet<>();

        // Default role assigned
        Role userRole = roleRepository.findByName(ERole.ROLE_DEFAULT)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        // set all roles to user object
        user.setRoles(roles);

        // Save into the database
        userRepository.save(user);

        // return success MSG to frontEnd user is registered successfully
        return ResponseEntity.ok(new MessageResponseDto("User registered successfully!"));
    }

    // Get All Users
    public List<User> getAllUsers() {
        return new ArrayList<>(userDetailsServiceImpl.getAllUserDetails());
    }
}
