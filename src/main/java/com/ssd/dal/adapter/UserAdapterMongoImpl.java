package com.ssd.dal.adapter;

import com.ssd.dal.model.ERole;
import com.ssd.dal.model.Role;
import com.ssd.dal.model.User;
import com.ssd.dal.model.UserDetailsModel;
import com.ssd.dal.repo.RoleMongoRepository;
import com.ssd.dal.repo.UserMongoRepository;
import com.ssd.dto.JwtResponseDto;
import com.ssd.dto.MessageResponseDto;
import com.ssd.dto.UserLoginDto;
import com.ssd.dto.UserRegisterDto;
import com.ssd.security.jwt.JwtUtils;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserAdapterMongoImpl {

    private final UserMongoRepository userRepository;
    private final RoleMongoRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public UserAdapterMongoImpl(UserMongoRepository userRepository, RoleMongoRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    // User registration method
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDto userRegister)
            throws UnsupportedEncodingException, MessagingException {

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

        // Create new user's account
        User user = new User(userRegister.getUsername(), userRegister.getContactNo(),
                passwordEncoder.encode(userRegister.getPassword()), userRegister.getEmail(),
                userRegister.getUserType());

        // Create new HashSet to store user Roles
        Set<Role> roles = new HashSet<>();

        // Role assigned
        Role userRole = roleRepository.findByName(ERole.ROLE_DEFAULT)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        // set all roles to user object
        user.setRoles(roles);

        // Save all user details into the database
        userRepository.save(user);

        // return success MSG to frontEnd user is registered successfully
        return ResponseEntity.ok(new MessageResponseDto("User registered successfully!"));
    }

    // User authenticate and Login method
    public ResponseEntity<?> authUserLogin(@Valid @RequestBody UserLoginDto userLoginDto) {

        // Get username and password and create new AuthenticationToken
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword()));

        // Set above assigned user credentials using Authentication object
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // After that create new JWT Token for that person
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Then get authentication principles and set that UserDetailimpl object
        UserDetailsModel userDetails = (UserDetailsModel) authentication.getPrincipal();

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

    public List<User> getAllUserDetails() {
        return new ArrayList<>(userDetailsServiceImpl.getAllUserDetails());
    }
}