package com.enginerringdigest.JournalApp.Service;

import com.enginerringdigest.JournalApp.Entity.JournalEntry;
import com.enginerringdigest.JournalApp.Entity.User;
import com.enginerringdigest.JournalApp.Repository.JournalRepository;
import com.enginerringdigest.JournalApp.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveEntry(
            JournalEntry journalEntry,
            String userName) {

        try {

            User user =
                    userService.findByUserName(userName);

            JournalEntry saved =
                    journalRepository.save(journalEntry);

            user.getJournalEntryList().add(saved);

            userService.saveUser(user);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void saveEntry(
            JournalEntry journalEntry,
            ObjectId id) {

        User user = userService.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        JournalEntry saved =
                journalRepository.save(journalEntry);

        user.getJournalEntryList().add(saved);

        userService.saveUser(user);
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalRepository.save(journalEntry);
    }


    public List<JournalEntry> getAll() {
        return journalRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalRepository.findById(id);
    }
@Transactional
    public boolean deleteByid (
            ObjectId id,
            String userName) {
    boolean removed=false;
        User user =
                userService.findByUserName(userName);

         removed=user.getJournalEntryList()
                .removeIf(x -> x.getId().equals(id));
    try{
        if (removed) {
            userService.saveUser(user);

            journalRepository.deleteById(id);
        }
    }
        catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("an error occure"+ e);
    }

    return removed;

    }

    public User findByUserName(String userName){
     return   userRepository.findByUserName(userName);
    }
}