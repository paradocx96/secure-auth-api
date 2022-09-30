package com.ssd.dal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsModel implements UserDetails {

    private static final long serialVersionUID = 1L;
    private String id;
    private String username;
    private String contactNo;
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsModel(String id, String username, String contactNo, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.contactNo = contactNo;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsModel build(User user) {
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsModel(user.getId(),
                user.getUsername(),
                user.getContactNo(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        UserDetailsModel user = (UserDetailsModel) object;
        return Objects.equals(id, user.id);
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNo() {
        return contactNo;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
