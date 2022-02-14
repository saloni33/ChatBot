package com.example.chatbot;

// This MsgModal class will hold the data
// which we are getting from the API

public class MsgModal {

    private String cnt;

    public MsgModal(String cnt) {
        this.cnt = cnt;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }
}
