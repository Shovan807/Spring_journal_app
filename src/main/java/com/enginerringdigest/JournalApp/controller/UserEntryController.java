package com.enginerringdigest.JournalApp.controller;

import com.enginerringdigest.JournalApp.Entity.User;
import com.enginerringdigest.JournalApp.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserEntryController {

    private final UserService userService;

    public UserEntryController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(
            @RequestBody User user) {

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

    @GetMapping("/myId/{id}")
    public ResponseEntity<User> findById(
            @PathVariable ObjectId id) {

        User user = userService.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/myId/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable ObjectId id) {

        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}