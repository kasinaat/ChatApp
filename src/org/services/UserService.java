package org.services;

import java.io.*;
import java.util.*;
import org.main.Server;
import org.models.*;

public class UserService {
    static BufferedReader br = null;
    static Console console = null;
    public static List<User> users;
    public static List<Group> groups;

    @SuppressWarnings("unchecked")
    public static List<User> loadUsers() {
        try {
            users = new ArrayList<User>();
            FileInputStream fis = new FileInputStream(
                    "C:\\Users\\Administrator\\Desktop\\Assignments\\ChatApp\\lib\\users.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (List<User>) ois.readObject();
        } catch (Exception e) {

        }
        return users;
    }
    @SuppressWarnings("unchecked")
    public static List<Group> loadGroups() {
        try {
            groups = new ArrayList<Group>();
            FileInputStream fis = new FileInputStream(
                    "C:\\Users\\Administrator\\Desktop\\Assignments\\ChatApp\\lib\\groups.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            groups = (List<Group>) ois.readObject();
        } catch (Exception e) {

        }
        return groups;
    }
    public static void storeGroups() {
        try {
            FileOutputStream fos = new FileOutputStream(
                    "C:\\Users\\Administrator\\Desktop\\Assignments\\ChatApp\\lib\\groups.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(groups);
        } catch (Exception e) {

        }
    }

    public static void storeUsers() {
        try {
            FileOutputStream fos = new FileOutputStream(
                    "C:\\Users\\Administrator\\Desktop\\Assignments\\ChatApp\\lib\\users.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
        } catch (Exception e) {

        }
    }

    public static User login() {
        User user = new User();
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            console = System.console();
            System.out.println("_____LOGIN_____");
            System.out.print("Enter the username : ");
            user.setUsername(br.readLine());
            System.out.print("Enter Password(password will not be visible) : ");
            char[] pass = console.readPassword();
            user.setPassword(new String(pass));
            for (User each : users) {
                // System.out.println(each.getPassword()+" "+user.getPassword());
                if (each.compareTo(user) == 0) {
                    // System.out.println(user.getGroups());
                    return each;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }

    public static boolean signup() throws IOException, FileNotFoundException, ClassNotFoundException {
        System.out.println("_______SIGNUP_______");
        User user;
        String username;
        String password;
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            console = System.console();
            System.out.print("Enter the username : ");
            username = br.readLine();
            System.out.print("Enter Password(password will not be visible) : ");
            char[] pass = console.readPassword();
            password = (new String(pass));
            user = new User(username, password);
            users.add(user);
            FileOutputStream fos = new FileOutputStream(
                    "C:\\Users\\Administrator\\Desktop\\Assignments\\ChatApp\\lib\\users.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}