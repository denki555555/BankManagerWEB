package com.example.demo.User;

public class User {
    private int userid;
    private String name;
    private String password;

    public User(int id){
        this.userid=id;
    }

    public String getUserName(){
        return this.name;
    }

    public void setUserName(String name){
        this.name=name;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password=password;
    }
}
