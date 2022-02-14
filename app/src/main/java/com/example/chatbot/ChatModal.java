package com.example.chatbot;

// hold the data which we need to
// display in recycler view

public class ChatModal {

    private String message;
    private String sender;   // who is the sender user or bot

    public ChatModal(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
