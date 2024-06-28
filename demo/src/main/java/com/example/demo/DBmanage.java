package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.example.demo.User.User;

@Repository
public class DBmanage {

    static final String url="jdbc:postgresql://dpg-cpv8c5tumphs73c7pd1g-a:5432/bankaccounts";
    static final String user="user2";
    static final String password="user2";
    
    //DBからアカウント情報を読み込むメソッド
    public ArrayList<Account> getAccountInfoFromDB(){

        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;

        ArrayList<Account> accounts =new ArrayList<>();
        try{
            // DB connect
            conn=DriverManager.getConnection(DBmanage.url,DBmanage.user,DBmanage.password);
            // Create Statement
            stmt=conn.createStatement();
            // Do SQL query
            String sql="SELECT * FROM BankAccounts";
            rs=stmt.executeQuery(sql);
            // Process Result
            while(rs.next()){
                Account account=new Account(rs.getString("accountHolderName"));
                account.setAccountNumber(rs.getInt("accountNumber"));
                account.deposit(rs.getDouble("balance"));
                accounts.add(account);
            }
            System.out.println("got users from DB:"+accounts);
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            // Close Resource
            try{
                if(rs!=null) rs.close();
                if(stmt!=null) stmt.close();
                if(conn!=null) conn.close();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return accounts;
    }

    //Bankクラスで更新した内容をDB側に反映させるメソッド
    public void updateAccount(Account account){

        try(Connection conn=DriverManager.getConnection(DBmanage.url,DBmanage.user,DBmanage.password);
            PreparedStatement pstmt = conn.prepareStatement("UPDATE BankAccounts SET accountHolderName=?,Balance=? WHERE accountNumber=?")){
            pstmt.setString(1, account.getAccountHolderName());
            pstmt.setDouble(2, account.getBalance());
            pstmt.setInt(3,account.getAccountNumber());
            pstmt.executeUpdate();

        }catch(SQLException e){
            System.out.println("SQLException detail is following:");
            e.printStackTrace();
        }catch(Exception e){
            System.out.println("not SQLException but Exception detail is following:");
            e.printStackTrace();
        }
    }

    //Bankクラスで新しく生成したアカウントをDBに挿入するメソッド
    public int insertNewAccount(Account account){
        try (Connection conn=DriverManager.getConnection(DBmanage.url,DBmanage.user,DBmanage.password);
            //DBにINSERTした後、AUTO_INCREMENT設定で自動採番された値を取得しにいく書き方
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO BankAccounts (accountHolderName,Balance) VALUES(?,?)",PreparedStatement.RETURN_GENERATED_KEYS)) {
                
            pstmt.setString(1, account.getAccountHolderName());
            pstmt.setDouble(2, 0);
            pstmt.executeUpdate();
            ResultSet generatedKeys=pstmt.getGeneratedKeys();
            //ResultSetからデータ取得する際の、カーソル位置の変更
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //UserテーブルからUser情報を一式取得するメソッド
    public ArrayList<User> getUsersFromDB(){
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        ArrayList<User> users=new ArrayList<>();
        try{
            //DB Connect
            conn=DriverManager.getConnection(DBmanage.url,DBmanage.user,DBmanage.password);
            stmt=conn.createStatement();
            String sql="SELECT * FROM Users";
            rs=stmt.executeQuery(sql);
            while(rs.next()){
                User oneUser = new User(rs.getInt("id"));
                oneUser.setUserName(rs.getString("name"));
                oneUser.setPassword(rs.getString("password"));
                users.add(oneUser);
            }
            for(User user:users){
                System.out.println("result of getUsersFromDB method:"+user.getUserName());
            }
        }catch(Exception e){
            System.out.println("error at getUsersFromDB method");
            e.printStackTrace();
        }finally{
            //close Resource
            try{
                if(rs!=null) rs.close();
                if(stmt!=null) stmt.close();
                if(conn!=null) conn.close();
            }catch(Exception e){
                System.out.println("error at closing resource of getUsersFromDB method");
                e.printStackTrace();
            }
        }
        return users;
    }

    //新しいユーザーを追加　※既存ユーザーと重複したnameになってないかチェック
    public boolean createUser(String name,String password){
        boolean checkUser=true;
        ArrayList<User> userList = this.getUsersFromDB();
        for(User user:userList){
            if(name.equals(user.getUserName())){
                checkUser=false;
                System.out.println("Duplicated username at createUser method:"+user.getUserName());
                break;
            }
        }
        System.out.println("send name to createUser method:"+name);
        System.out.println("result of duplicate of createUser method:"+checkUser);
        
        if(checkUser){
            try(Connection conn=DriverManager.getConnection(DBmanage.url, DBmanage.user, DBmanage.password);
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Users(name,password) VALUES(?,?)")){
                    pstmt.setString(1, name);
                    pstmt.setString(2, password);
                    pstmt.executeUpdate();
                    checkUser=true;
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            checkUser=false;
        }
        System.out.println("created users by createUser method:"+checkUser);
        return checkUser;
    }

    //PWチェック
    public boolean checkUserAccount(String name,String password){
        //ユーザー認証時のチェック用変数
        boolean passwordChecker=false;

        try(Connection conn=DriverManager.getConnection(DBmanage.url, DBmanage.user, DBmanage.password);
            PreparedStatement pstmt=conn.prepareStatement("SELECT name,password FROM Users WHERE name =?")){
                pstmt.setString(1, name);
                ResultSet rs=pstmt.executeQuery();
                if(rs.next()){
                    if(password.equals(rs.getString("password"))){
                        passwordChecker=true;
                    }else{
                        System.out.println("missed password");
                    }
                }else{
                    System.out.println("don't exist user that you selected");
                }

        }catch(Exception e){
            System.out.println("Excption occured");
            e.printStackTrace();
        }
        return passwordChecker;
    }
}