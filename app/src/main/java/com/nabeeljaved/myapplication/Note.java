package com.nabeeljaved.myapplication;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Note {
    private String documentId;
    private String title;
    private String description;
    private int priority;
    List<String> subjects;

    public Note() {
        //public no-arg constructor needed
    }

    public Note(String title, String description, int priority, List<String> subjects) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.subjects = subjects;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public List<String> getSubjects() {
        return subjects;
    }
}