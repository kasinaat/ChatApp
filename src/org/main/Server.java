package org.main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.regex.*;

import org.models.*;
import org.services.UserService;

public class Server {
    List<ClientThread> activeClients;
    private int port;

    public Server(int port) {
        this.port = port;
        activeClients = new ArrayList<ClientThread>();
    }

    public void init() {
        try {
            ServerSocket server = new ServerSocket(port);
            boolean flag = true;
            while (flag) {
                Socket socket = server.accept();
                ClientThread t = new ClientThread(socket);
                if (!flag)
                    break;
                activeClients.add(t);
                t.start();
            }
            try {
                server.close();
                for (int i = 0; i < activeClients.size(); ++i) {
                    ClientThread tc = activeClients.get(i);
                    try {
                        System.out.println("Closed");
                        // close all data streams and socket
                        tc.ois.close();
                        tc.oos.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                    }
                }
            } catch (Exception e) {
                System.out.println("client and server closed");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        int portNum = 8888;
        Server server = new Server(portNum);
        server.init();

    }

    class ClientThread extends Thread {
        Socket socket;
        ObjectInputStream ois;
        ObjectOutputStream oos;
        User user;
        Message message;
        public List<User> users;

        public void setUser(User user) {
            this.user = user;
        }

        public User getUser() {
            return this.user;
        }

        public ClientThread(Socket socket) {
            this.socket = socket;
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                user = (User) ois.readObject();
                System.out.println(user.getUsername());
                System.out.println(user.getGroups());
            } catch (IOException ioe) {
                System.out.println("Cannot create new I/O");
            } catch (ClassNotFoundException cnfe) {

            }
        }

        private synchronized boolean send(Message message) {
            boolean found = false;
            try {
                String[] arg = message.getMessage().split(" ");
                String user = arg[0].substring(1);
                System.out.println(user);
                int start = message.getMessage().indexOf(" ");
                String content = message.getSender() + ":" + message.getMessage().substring(start + 1);
                System.out.println(content + " " + start);
                for (int i = 0; i < activeClients.size(); i++) {
                    ClientThread temp = activeClients.get(i);
                    String match = temp.getUser().getUsername();
                    if (match.equals(user)) {
                        System.out.println("User found!!!");
                        if (!temp.write(content)) {
                            activeClients.remove(temp);
                            System.out.println("Sorry selected client is offline");
                        }
                        found = true;
                        break;
                    }
                }

            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please enter valid command");
            }
            if (found == false)
                return false;
            return true;

        }

        private synchronized void remove(User user) {
            for (int i = 0; i < activeClients.size(); ++i) {
                ClientThread ct = activeClients.get(i);
                // if found remove it
                if (ct.getUser().equals(user)) {
                    activeClients.remove(i);
                    break;
                }
            }
        }

        private synchronized boolean sendGroup(Message message) {
            System.out.println("Inside Send group");
            List<Group> groups = UserService.loadGroups();
            System.out.println(groups);
            for (Group each : groups) {
                System.out.println(each.getName());
                if (each.getName().equals(message.getReceiver())) {
                    for (User one : each.getUsers()) {
                        if (one.getUsername().equals(user.getUsername()))
                            continue;
                        Message msg = new Message();
                        msg.setSender(user.getUsername());
                        msg.setMessage("#" + one.getUsername() + " " + "From Group " + each.getName() + "->"
                                + message.getMessage());
                        System.out.println(msg.getMessage());
                        send(msg);
                    }
                }
            }
            return false;
        }

        public void run() {
            boolean flag1 = true;

            while (flag1) {
                this.users = UserService.loadUsers();
                for (User each : this.users) {
                    if (each.getUsername().equals(user.getUsername()))
                        each = user;
                }
                try {
                    message = (Message) ois.readObject();
                } catch (IOException e) {
                    System.out.println("IO error occured");
                    break;
                } catch (ClassNotFoundException ce) {
                    break;
                }
                Pattern pattern = Pattern.compile("#[a-zA-Z0-9 ]+#");
                String msg = message.getMessage();
                String[] arg = msg.split(" ");
                // System.out.println(msg + " "+arg[0]);
                Matcher match = pattern.matcher(msg);
                if (arg[0].equalsIgnoreCase("#online")) {
                    write("List of Users Online ");
                    for (int i = 0; i < activeClients.size(); i++) {
                        ClientThread temp = activeClients.get(i);
                        write("_>" + temp.getUser().getUsername());
                    }
                } else if (arg[0].equalsIgnoreCase("#logout")) {
                    System.out.println("Logged Out Successfully");
                    flag1 = false;
                } else if (match.find()) {
                    System.out.println("Sending to group");
                    // System.out.println(match.start() +" " + match.end());
                    // System.out.println(n.substring(match.start()+1,match.end()-1));
                    message.setReceiver(msg.substring(match.start() + 1, match.end() - 1));
                    System.out.println("Receiver = " + message.getReceiver());
                    message.setMessage(msg.replaceFirst(msg.substring(match.start(), match.end()), ""));
                    System.out.println(message.getMessage());
                    sendGroup(message);
                } else {
                    send(message);
                }
            }
            // TODO
            remove(user);
        }

        private boolean write(String message) {
            try {
                oos.writeObject(new Message(message));
                oos.flush();
            } catch (IOException ioe) {
                System.out.println("Cannot fetch active users");
                ioe.printStackTrace();
                return false;
            }
            return true;
        }

        private void close() {
            System.out.println("Closed Data Streams");
            try {
                if (oos != null)
                    oos.close();
            } catch (Exception e) {
            }
            try {
                if (ois != null)
                    ois.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (socket != null)
                    socket.close();
            } catch (Exception e) {
            }
        }
    }
}
