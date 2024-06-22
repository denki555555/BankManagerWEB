package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping(value = "/login")
public class loginController {
    @PostMapping("/CreateNewUser")
    public String CreateNewUserMethod() {
        return "CreateUserPage";
    }
    
    @PostMapping("/CreateUser")
    public String MoveCreatePageMethod(@RequestParam("name") String name,@RequestParam("password") String password) {
        DBmanage dbmanage = new DBmanage();
        boolean createdCheck=dbmanage.createUser(name, password);
        if(createdCheck){
            return "CreatedUserPage";
        }else{
            return "InvalidCreatedUserPage";
        }
    }
    
    @PostMapping("/InputUserInfo")
    public String MoveLoginPageMethod() {
        return "loginPage";
    }

    @PostMapping("/SelectOperate")
    public String SelectOperateMethod() {
        return "startPage";
    }
    
    @PostMapping("/CheckUserInfo")
    public String CheckUserInfoMethod(@RequestParam("name") String name,@RequestParam("password") String password){
        DBmanage dbmanage = new DBmanage();
        boolean checkAccount=dbmanage.checkUserAccount(name, password);
        if(checkAccount){
            return "startPage";
        }else{
            return "InvalidUserInfoPage";
        }
    }
}
