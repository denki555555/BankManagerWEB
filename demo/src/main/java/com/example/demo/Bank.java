package com.example.demo;

import java.util.ArrayList;

public class Bank {
    static ArrayList<Account> accounts = new ArrayList<>();

    private static DBmanage dbmanage = new DBmanage();

    static{
        accounts=dbmanage.getAccountInfoFromDB();
    }

    //口座情報を新しく作る
    public Account createAccount(String accountHolderName){
        //新しい口座インスタンスを生成
        Account newAccount = new Account(accountHolderName);
        //生成したAccountをAccountListに追加
        accounts.add(newAccount);
        System.out.println(accountHolderName+"様の口座が作成されました");
        int newAccountNum = dbmanage.insertNewAccount(newAccount);
        newAccount.setAccountNum(newAccountNum);
        return newAccount;
    }

    //指定された口座に預金する
    public boolean depositToAccount(int accountNumber,double amount){
        //口座の有無のチェック用変数を用意
        boolean found=false;
        //AccountListの中から指定されたAccountNumberを持つ口座を探す
        Account targetAccount=checkAccount(accountNumber);
        if(targetAccount!=null){
            targetAccount.deposit(amount);
            found=true;
            dbmanage.updateAccount(targetAccount);
        }
        return found;
    }

    //指定された口座から引き出す
    public boolean withdrawFromToAccount(int AccountNumber,double amount){
        boolean checkBalance=false;
        Account targetAccount=checkAccount(AccountNumber);
        if(targetAccount!=null){
            checkBalance=targetAccount.withdraw(amount);
            dbmanage.updateAccount(targetAccount);
        }
        return checkBalance;
    }

    //口座残高を見る
    public double getAccountBalance(int accountNumber){
        Account targeAccount=checkAccount(accountNumber);
        if(targeAccount!=null){
            return targeAccount.getBalance();
        }else{
            return 0;
        }
    }

    //口座一覧を見る
    public ArrayList<Account> listAllAccounts(){
        return dbmanage.getAccountInfoFromDB();
    }

    //****************サブメソッド群***********************
    //指定されたAccountNumberでAccountを検索する
    public static Account checkAccount(int accountNumber){
        Account returnAccount=null;
        for(Account ac:accounts){
            if(ac.getAccountNumber()==accountNumber){
                returnAccount=ac;
            }
        }
        if(returnAccount==null){
            System.out.println("指定された口座が見つかりませんでした");
        }
        return returnAccount;
    }
}
