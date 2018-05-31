package org.models;

import java.io.*;
import java.util.*;

public class Group implements Serializable {
    private static final long serialVersionUID = 595015L;
    private String name;
    private List<User> users;

    public Group() {
        users = new ArrayList<User>();
    }

    public Group(String name) {
        this.name = name;
        users = new ArrayList<User>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}