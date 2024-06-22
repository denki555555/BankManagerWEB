package com.example.demo.User;

import java.util.ArrayList;

import com.example.demo.DBmanage;

public class UserManage{

    private static ArrayList<User> userList = new ArrayList<>();

    private static DBmanage dbmanage = new DBmanage();

    static{
        userList=dbmanage.getUsersFromDB();
        System.out.println("ユーザーデータベースから情報を取得します");
        System.out.println(userList);
    }

    //ユーザー名の重複チェック
    public boolean checkDuplicate(String name){
        //チェック用変数を用意
        boolean checkDupli=false;
        for(User user:userList){
            if(name.equals(user.getUserName())){
                checkDupli=false;
            }
        }
        return checkDupli;
    }
    
}


