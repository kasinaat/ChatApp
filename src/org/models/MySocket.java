package org.models;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MySocket extends Socket {

    private String socketName;
    public MySocket(){}
    public MySocket(String hostname,int port) throws UnknownHostException,IOException{
        super(hostname,port);
    }

    public void setSocketName(String socketName) {
        this.socketName = socketName;
    }

    public String getSocketName() {
        return this.socketName;
    }
}