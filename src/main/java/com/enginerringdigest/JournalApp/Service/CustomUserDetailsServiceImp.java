package com.enginerringdigest.JournalApp.Service;

import com.enginerringdigest.JournalApp.Entity.User;
import com.enginerringdigest.JournalApp.Repository.UserRepository;
import org.apache.el.lang.ELArithmetic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceImp implements UserDetailsService {
    private  final  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        System.out.println("USERNAME RECEIVED = [" + username + "]");

        User user = userRepository.findByUserName(username);

        if (user == null) {
            System.out.println("USER NOT FOUND");
            throw new UsernameNotFoundException(
                    "User not found: " + username
            );
        }

        System.out.println("USER FOUND = [" + user.getUserName() + "]");
        System.out.println("PASSWORD FROM DB = [" + user.getPassword() + "]");

        boolean result = passwordEncoder.matches(
                "your_actual_password_here",
                user.getPassword()
        );


        System.out.println("PASSWORD MATCH = " + result);


        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))
                .build();
    }
}