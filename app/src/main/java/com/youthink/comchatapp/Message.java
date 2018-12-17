package com.youthink.comchatapp;

public class Message {
    private String msg;
    private String timeStamp;
    private String userId;

    public Message(){

    }

    public Message(String msg, String timeStamp, String userId){
        this.msg = msg;
        this.timeStamp = timeStamp;
        this.userId= userId;
    }

    public String getMsg(){
        return msg;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }

    public String getTimeStamp(){
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp){
        this.timeStamp = timeStamp;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }
}