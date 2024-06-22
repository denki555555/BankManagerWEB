package com.example.demo;

public class Account {
    private int accountNumber;
    private String accountHolderName;
    private double Balance=0;

    public Account(String Name){
        this.accountHolderName=Name;
    }

    public int getAccountNumber(){
        return this.accountNumber;
    }

    public void setAccountNumber(int accountNumber){
        this.accountNumber=accountNumber;
    }

    //預金
    public void deposit(double amount){
        this.Balance+=amount;
    }
    
    //引き出し
    public Boolean withdraw(double amount){
        boolean check=false;
        if(this.Balance>=amount){
            this.Balance-=amount;
            check=true;
        }else{
            System.out.println("残高が足りません");
        }
        return check;
    }

    //残高照会
    public double getBalance(){
        return this.Balance;
    }

    //口座名義人名称取り出し
    public String getAccountHolderName(){
        return this.accountHolderName;
    }

    //accountNumberを指定するメソッド（INSERTした後のNum取得用）
    public void setAccountNum(int accountNumber){
        this.accountNumber=accountNumber;
    }
}
