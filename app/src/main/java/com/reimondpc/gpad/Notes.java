package com.reimondpc.gpad;

public class Notes {

    private int idNote;
    private String title;
    private String content;

    public Notes() {
    }

    public Notes(int idNote, String title, String content) {
        this.idNote = idNote;
        this.title = title;
        this.content = content;
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
}
