package com.aspegrenide.multiknapp;

public class Message {
    private String messageTextSV;
    private String messageSubjectSV;
    private String messageTextEN;
    private String messageSubjectEN;
    private String messageTextFA;
    private String messageSubjectFA;
    private String videoPath;
    private long timeStamp;

    // for Firebase connection, empty constructor
    public Message() {
    }

    public Message(String messageTextSV, String messageSubjectSV, String videoPath, long timeStamp) {
        this.messageTextSV = messageTextSV;
        this.messageSubjectSV = messageSubjectSV;
        this.videoPath = videoPath;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageTextSV='" + messageTextSV + '\'' +
                ", messageSubjectSV='" + messageSubjectSV + '\'' +
                ", messageTextEN='" + messageTextEN + '\'' +
                ", messageSubjectEN='" + messageSubjectEN + '\'' +
                ", messageTextFA='" + messageTextFA + '\'' +
                ", messageSubjectFA='" + messageSubjectFA + '\'' +
                ", videoPath='" + videoPath + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }



    public String getMessageTextSV() {
        return messageTextSV;
    }

    public void setMessageTextSV(String messageTextSV) {
        this.messageTextSV = messageTextSV;
    }

    public String getMessageSubjectSV() {
        return messageSubjectSV;
    }

    public void setMessageSubjectSV(String messageSubjectSV) {
        this.messageSubjectSV = messageSubjectSV;
    }

    public String getMessageTextEN() {
        return messageTextEN;
    }

    public void setMessageTextEN(String messageTextEN) {
        this.messageTextEN = messageTextEN;
    }

    public String getMessageSubjectEN() {
        return messageSubjectEN;
    }

    public void setMessageSubjectEN(String messageSubjectEN) {
        this.messageSubjectEN = messageSubjectEN;
    }

    public String getMessageTextFA() {
        return messageTextFA;
    }

    public void setMessageTextFA(String messageTextFA) {
        this.messageTextFA = messageTextFA;
    }

    public String getMessageSubjectFA() {
        return messageSubjectFA;
    }

    public void setMessageSubjectFA(String messageSubjectFA) {
        this.messageSubjectFA = messageSubjectFA;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}