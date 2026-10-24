package com.enginerringdigest.JournalApp.Entity;

import jakarta.annotation.Nonnull;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document("journal_entries")
//  class data is a document typ
@Data
@NoArgsConstructor
public class JournalEntry {
    //A POJO class means Plain Old Java Object.
    @Id
    private ObjectId id;
    private LocalDateTime localDateTime;
    @Nonnull
    private  String title;
    @Nonnull
    private  String content;



}
