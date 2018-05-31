package org.models;

import java.io.*;
import java.net.Socket;
import org.services.*;

public class Client extends Thread {
    // String friendName = null;
    private User user;
    BufferedReader br;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket socket;
    private int port;
    String server;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public Client(String server, int port, User user) {
        this.server = server;
        this.port = port;
        this.user = user;
    }

    public boolean init() {
        try {
            socket = new Socket(server, port);
        } catch (Exception e) {
            System.out.println("Cannot connect to server");
            return false;
        }

        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Cannot read / Write");
            return false;
        }
        new Receiver().start();
        try {
            oos.writeObject(user);
        } catch (IOException ioe) {
            System.out.println("Cannot login");
        }
        return true;
    }

    public void send(Message message) {
        try {
            oos.writeObject(message);
        } catch (IOException ioe) {
            System.out.println("Cannot send message!");
        }
    }

    private void logout() {
        try {
            if (ois != null)
                ois.close();
        } catch (Exception e) {
        }
        try {
            if (oos != null)
                oos.close();
        } catch (Exception e) {
        }
        try {
            if (socket != null)
                socket.close();
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        UserService.loadUsers();
        int portNum = 8888;
        User user = null;
        int option = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("___________ChatApp____________");
            System.out.println("1.Login\n2.Signup");
            System.out.print("Choose any option :");
            option = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            // TODO: handle exception
        }

        switch (option) {
        case 1:
            try {
                user = UserService.login();
                boolean clientFlag = true;
               
                if (user != null) {
                    System.out.println("Logged In Successfully!");
                    Client client = new Client("localhost", portNum, user);
                    if (!client.init())
                        return;
                    // TODO Instuctions
                    while (clientFlag) {
                        String message = br.readLine();
                        Message content = new Message(message);
                        content.setSender(user.getUsername());
                        if (content.getMessage().equals("#LOGOUT")) {
                            client.send(content);
                            clientFlag = false;
                        }
                        client.send(content);
                    }
                    br.close();
                    client.logout();

                } else {
                    System.out.println("wrong credentials");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            break;

        case 2:
            try {
                if (UserService.signup()) {
                    System.out.println("Signed up sucessfully!!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            break;

        default:
            System.out.println("Enter Correct Option!!!");
            break;
        }
    }

    class Receiver extends Thread {
        public void run() {
            while (true) {
                try {
                    Message message = (Message) ois.readObject();
                    System.out.println(message.getMessage());
                } catch (IOException e) {
                    System.out.println("Connection Closed");
                    break;
                } catch (ClassNotFoundException c) {
                    break;
                }
            }
        }
    }
}
