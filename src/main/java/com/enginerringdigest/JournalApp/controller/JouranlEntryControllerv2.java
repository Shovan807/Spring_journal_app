package com.enginerringdigest.JournalApp.controller;

import com.enginerringdigest.JournalApp.Entity.JournalEntry;
import com.enginerringdigest.JournalApp.Entity.User;
import com.enginerringdigest.JournalApp.Service.JournalEntryService;
import com.enginerringdigest.JournalApp.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JouranlEntryControllerv2 {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalentriesOfUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
      User user=userService.findByUserName(userName);

        List<JournalEntry> all=user.getJournalEntryList();
        if (all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }


    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();

        System.out.println("Username = " + userName);
        System.out.println("Title = " + myEntry.getTitle());
        System.out.println("Content = " + myEntry.getContent());


        try {

            journalEntryService.saveEntry(myEntry,userName);

            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


/*
    @PostMapping("{id}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry,@PathVariable ObjectId id) {
        System.out.println("id = " + id);
        System.out.println("Title = " + myEntry.getTitle());
        System.out.println("Content = " + myEntry.getContent());


        try {

            journalEntryService.saveEntry(myEntry,id);

            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
*/
@GetMapping("myId/{id}")
public ResponseEntity<JournalEntry> findById(@PathVariable ObjectId id) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    String userName = authentication.getName();

    User user = userService.findByUserName(userName);

    Optional<JournalEntry> journalEntry = user.getJournalEntryList()
            .stream()
            .filter(x -> x.getId().equals(id))
            .findFirst();

    if (journalEntry.isPresent()) {
        return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
    }

    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}



    @DeleteMapping("myId/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable ObjectId id) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
      boolean removed=  journalEntryService.deleteByid(id,userName);
      if (removed){
          return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      else{
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    }


    @PutMapping("id/{id}")
    public ResponseEntity<JournalEntry> updateJournalById(
            @PathVariable ObjectId id,
            @RequestBody JournalEntry newEntry) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();

        User user = userService.findByUserName(userName);

        Optional<JournalEntry> optionalEntry = user.getJournalEntryList()
                .stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();

        if (optionalEntry.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        JournalEntry old = optionalEntry.get();

        if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) {
            old.setTitle(newEntry.getTitle());
        }

        if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
            old.setContent(newEntry.getContent());
        }

        journalEntryService.saveEntry(old, userName);

        return new ResponseEntity<>(old, HttpStatus.OK);
    }

}
