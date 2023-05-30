package com.projects.instaclient.model;

import java.io.File;
import java.util.ArrayList;

public class User {
    private String id = "";
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private File profileImage;
    private ArrayList<Long> posts;

    public User(String id, String firstName, String lastName, String email, String password, File profileImage, ArrayList<Long> posts) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.email = email;
        this.password = password;
    }

    public User(String firstName, String lastName, String email, String password, File profileImage, ArrayList<Integer> posts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.email = email;
        this.password = password;
    }

    // only for login post
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // only for update profile post
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public File getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(File profileImage) {
        this.profileImage = profileImage;
    }

    public ArrayList<Long> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Long> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
