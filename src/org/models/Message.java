package org.models;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 5950169519310163575L;
    private String sender;
    private String receiver;
    private String message;
    private String groupName;
    public Message(String message){
        this.message = message;
    }
    public Message() {
        
    }
    public String getSender(){
        return this.sender;
    }
    public void setSender(String sender){
        this.sender = sender;
    }
    public String getReceiver(){
        return this.receiver;
    }
    public void setReceiver(String receiver){
        this.receiver = receiver;
    }
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}