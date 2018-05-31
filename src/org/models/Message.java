package org.models;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 5950169519310163575L;
    private String sender;
    private String message;
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
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}