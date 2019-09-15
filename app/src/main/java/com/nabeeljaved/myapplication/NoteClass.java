package com.nabeeljaved.myapplication;

import com.google.firebase.firestore.Exclude;

public class NoteClass
{
    private String title;
    private String description;
    private String documentId;
    private int priority;

    public NoteClass()
    {
        //firebase need empty constructor by default no arg needed
    }

    public NoteClass(String title, String desc, int priority)
    {
        this.title = title;
        this.description = desc;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }



    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Exclude
    public String getDocumentId() {

        return documentId;
    }


    public String getTitle()
    {
        return title;
    }

    public String getDescription() {
        return description;
    }
}

