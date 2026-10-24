package com.enginerringdigest.JournalApp.controller;

import com.enginerringdigest.JournalApp.Entity.User;
import com.enginerringdigest.JournalApp.Repository.UserRepository;
import com.enginerringdigest.JournalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class publicController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ok";
    }

    //create user
    @PostMapping("/create-user")
    public ResponseEntity<?> create(@RequestBody User user){
        {

            try {

                userService.saveNewUser(user);

                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .build();

            } catch (Exception e) {

                e.printStackTrace();

                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Could not create user");
            }
        }
    }


    // CHECK CURRENTLY LOGGED-IN USER
    @GetMapping
    public ResponseEntity<?> checkUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        // Check authentication
        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return new ResponseEntity<>(
                    "User is not authenticated",
                    HttpStatus.UNAUTHORIZED
            );
        }

        // Get username from Basic Auth
        String userName = authentication.getName();

        // Find user in MongoDB
        User user =
                userService.findByUserName(userName);

        if (user == null) {
            return new ResponseEntity<>(
                    "User not found",
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                "Authenticated user: " + userName,
                HttpStatus.OK
        );
    }


    @PutMapping
    public ResponseEntity<?> update(
            @RequestBody User user) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String userName = authentication.getName();

        User userInDb =
                userService.findByUserName(userName);

        if (userInDb == null) {
            return new ResponseEntity<>(
                    "User not found",
                    HttpStatus.NOT_FOUND
            );
        }

        if (user.getUserName() != null &&
                !user.getUserName().isEmpty()) {

            userInDb.setUserName(user.getUserName());
        }

        if (user.getPassword() != null &&
                !user.getPassword().isEmpty()) {

            userInDb.setPassword(
                    userService.encodePassword(
                            user.getPassword()
                    )
            );
        }

        userService.saveUser(userInDb);

        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }

    @DeleteMapping
    public  ResponseEntity<?> deleteUserById(){
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}