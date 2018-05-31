package org.models;
import java.net.ServerSocket;
import java.net.SocketException;
import java.io.*;

public class MyServerSocket extends ServerSocket {
    public MyServerSocket(int port) throws IOException {
        super(port, 50, null);
    }
    public MySocket accept() throws IOException {
        if (isClosed())
            throw new SocketException("Socket is closed");
        if (!isBound())
            throw new SocketException("Socket is not bound yet");
        MySocket s = new MySocket();
        implAccept(s);
        return s;
    }
    
}