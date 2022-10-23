package com.alvesjv.projecthexagonalfull.app.utils;

import com.alvesjv.projecthexagonalfull.app.domain.entities.User;
import com.alvesjv.projecthexagonalfull.app.domain.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UserTestUtils {

    public static User createUser(Role role){
        User user = new User();
        user.setIdUser(UUID.randomUUID());
        user.setUsername("JOAO");
        user.setPassword("12345");
        user.setRole(role);

        return user;
    }

    public static List<User> createListUser(){
        return Arrays.asList(createUser(Role.USER));
    }

    public static UserDetails getUserDetails(){
        User user = createUser(Role.USER);
        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getUsername();
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
                return true;
            }
        };

        return userDetails;
    }
}
