package com.alvesjv.projecthexagonalfull.app.domain.core;

import com.alvesjv.projecthexagonalfull.app.domain.entities.User;
import com.alvesjv.projecthexagonalfull.app.domain.exception.CarException;
import com.alvesjv.projecthexagonalfull.app.domain.exception.CustomException;
import com.alvesjv.projecthexagonalfull.app.ports.out.DataBaseIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityCore {

    @Autowired
    @Qualifier("user")
    private DataBaseIntegration userDataBase;

    public User getCurrentUser(){
        try {

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetails)principal).getUsername();

            User userFilter = new User();
            userFilter.setUsername(username);

            Example<User> ex = Example.of(userFilter);

            Optional<User> user = userDataBase.findOne(ex);

            if(!user.isPresent()){
                throw new CarException("User not found", HttpStatus.BAD_REQUEST);
            }

            return user.get();

        }catch (Throwable t){
            throw new CustomException(t);
        }
    }
}
