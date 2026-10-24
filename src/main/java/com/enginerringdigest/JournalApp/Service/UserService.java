package com.enginerringdigest.JournalApp.Service;

import com.enginerringdigest.JournalApp.Entity.User;
import com.enginerringdigest.JournalApp.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }


    // ONLY FOR REGISTRATION
    public void saveNewUser(User user) {

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );
        user.setRoles(Arrays.asList("USER"));

        userRepository.save(user);
    }


    // FOR EXISTING USER
    public void saveUser(User user) {

        userRepository.save(user);
    }


    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }


    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }


    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void saveAdmin(User user) {
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );
        user.setRoles(Arrays.asList("USER","ADMIN"));

        userRepository.save(user);
    }
}