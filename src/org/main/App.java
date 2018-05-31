package org.main;

import java.io.*;

import org.services.*;
import org.models.*;

public class App {
    public static void main(String[] args) throws IOException,InterruptedException,NotSerializableException {
        try {
            Server.loadUsers();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("___________ChatApp____________");
        System.out.println("1.Login\n2.Signup");
        System.out.print("Choose any option :");
        int option = Integer.parseInt(br.readLine());
        switch (option) {
        case 1:
            try {
                User user = UserService.login();
                if (user != null) {
                    System.out.println("Logged In Successfully!");
                    UserService.menu(user);
                } else {
                    System.out.println("wrong credentials");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            break;

        case 2:
            try {
                if (UserService.signup()) {
                    System.out.println("Signed up sucessfully!!");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            break;

        default:
            System.out.println("Enter Correct Option!!!");
            break;
        }
    }
}