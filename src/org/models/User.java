/*
 @Author Kasinaat Selvi Sukesh
 */
package org.models;
import java.io.*;
import java.net.*;

public class User implements Serializable,Comparable<User>{
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    // private MySocket socket;
    

    public User(){}
    public User(String username,String password){
        this.username = username;
        this.password = password;
        // socket = new MySocket("localhost",port);
        // socket.setSocketName(this.username);
    }
    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }
    
    @Override
    public int compareTo(User another){
        if((this.username).equals(another.getUsername()) && (this.password).equals(another.getPassword())){
            // System.out.println("Equals");
            return 0;
        }
        return -1;
    }
}