package com.enginerringdigest.JournalApp.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document("user")
@Data
public class User {
    //A POJO class means Plain Old Java Object.
    @Id
    private ObjectId id;
    @Nonnull
    private String password;
    @Indexed(unique = true)
    @Nonnull
    private String userName;

    @DBRef
    private List<JournalEntry> journalEntryList=new ArrayList<>();
    private List<String> roles;


}
