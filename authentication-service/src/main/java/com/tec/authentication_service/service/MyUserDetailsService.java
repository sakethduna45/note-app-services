package com.tec.authentication_service.service;

import com.tec.authentication_service.model.User;
import com.tec.authentication_service.model.UserPrinciple;
import com.tec.authentication_service.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {



    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = repo.findByUsername(username);

        if(user == null){
            throw  new UsernameNotFoundException("User not found");
        }



        return new UserPrinciple(user);
    }





}
