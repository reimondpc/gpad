package com.reimondpc.gpad;

public class Notes {

    private int idNote;
    private String title;
    private String content;
    private String timestamp;


    public Notes() {
    }

    public Notes(int idNote, String title, String content, String timestamp) {
        this.idNote = idNote;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
