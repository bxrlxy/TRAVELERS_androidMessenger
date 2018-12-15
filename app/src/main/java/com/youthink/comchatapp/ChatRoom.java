package com.youthink.comchatapp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatRoom {
    private String title;
    private String hostName;
    private String remains;
    private String deadline;
    private String content;
    private String location;
    private String timeStamp;
    private boolean isComplete;

    public ChatRoom(){

    }

    public ChatRoom(String title, String hostName, String remains, String deadline, String content, String location){
        this.title = title;
        this.hostName = hostName;
        this.remains = remains;
        this.deadline = deadline;
        this.content = content;
        this.location = location;
        this.isComplete = false;

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        this.timeStamp = ts;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getHostName(){
        return hostName;
    }

    public void setHostName(String hostName){
        this.hostName = hostName;
    }

    public String getRemains(){
        return remains;
    }

    public void setRemains(String remains){
        this.remains = remains;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getDeadline(){
        return deadline;
    }

    public void setDeadline(String deadline){
        this.deadline = deadline;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getTimeStamp(){
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp){
        this.timeStamp = timeStamp;
    }

    public boolean getIsComplete(){
        return isComplete;
    }

    public void setIsComplete(boolean IsComplete){
        this.isComplete = isComplete;
    }
}