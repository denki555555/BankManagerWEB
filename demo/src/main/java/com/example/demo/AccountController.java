package com.example.demo;

import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/Account")
public class AccountController {
    @PostMapping("/InputAccountNum")
    public String InputAccountNumMethod() {
        return "InputAccountNumPage";
    }

    @PostMapping("/ShowCreateAccountPage")
    public String ShowCreateAccountPageMethod() {
        return "CreateNewAccountPage";
    }

    @PostMapping("/CreateNewAccount")
    public ModelAndView CreateNewAccountMethod(@RequestParam("accountHolderName") String accountHolderName) {
        ModelAndView modelAndView=new ModelAndView("CreatedAccountPage");
        Bank bank=new Bank();
        Account newAccount=bank.createAccount(accountHolderName);
        //ModelAndViewに追加したい情報は口座番号と名義人名
        modelAndView.addObject("accountNumber", newAccount.getAccountNumber());
        modelAndView.addObject("accountHolderName",newAccount.getAccountHolderName());
        return modelAndView;
    }

    @PostMapping("/SelectAccount")
    public ModelAndView selectAccountMethod(@RequestParam("accountNumber") int accountNumber) {
        //Accountの存在チェック
        Account TargetAccount=Bank.checkAccount(accountNumber);
        
        ModelAndView modelAndView=new ModelAndView("SelectOperatePage");
        ModelAndView invalidModelAndView = new ModelAndView("InvalidAccountNumPage");

        if(TargetAccount!=null){
            modelAndView.addObject("accountNumber", accountNumber);
            return modelAndView;
        }else{
            return invalidModelAndView;
        }
    }

    @PostMapping("/depositAmountInput")
    public ModelAndView depositAmountInputMethod(@RequestParam("accountNum") int accountNumber) {
        ModelAndView modelAndView=new ModelAndView("InputDepositAmountPage");
        modelAndView.addObject("accountNumber", accountNumber);
        return modelAndView;
    }

    @PostMapping("/depositToAccount")
    public ModelAndView depositToAccountMethod(@RequestParam("accountNum") int accountNumber,@RequestParam("amount") double amount) {
        Bank bank=new Bank();
        ModelAndView modelAndView = new ModelAndView("DepositedPage");

        try{
            bank.depositToAccount(accountNumber, amount);
            //balanceを小数点削除してカンマ追加（フォーマットを修正）
            NumberFormat numberformat=NumberFormat.getIntegerInstance(Locale.US);
            String formattedNumber =numberformat.format(bank.getAccountBalance(accountNumber));
            modelAndView.addObject("balance", formattedNumber);
            modelAndView.addObject("accountNumber", accountNumber);

        }catch(Exception e){
            modelAndView.addObject("errorMessage", e);
            modelAndView.addObject("accountNumber", accountNumber);
        }
        return modelAndView;
    }
    
    @PostMapping("/WithdrawAmountInput")
    public ModelAndView withdrawAmountInputMethod(@RequestParam("accountNum") int accountNumber) {
        ModelAndView modelAndView=new ModelAndView("InputWithdrawAmountPage");
        modelAndView.addObject("accountNumber", accountNumber);
        return modelAndView;
    }

    @PostMapping("/withdrawFromToAccount")
    public ModelAndView withdrawFromAccountMethod(@RequestParam("accountNum") int accountNumber,@RequestParam("amount") double amount) {
        Bank bank=new Bank();

        boolean availabilityCheck=bank.withdrawFromToAccount(accountNumber, amount);

        if(availabilityCheck){
            ModelAndView modelAndView = new ModelAndView("WithdrawedPage");
            //balanceを小数点削除してカンマ追加（フォーマットを修正）
            NumberFormat numberformat=NumberFormat.getIntegerInstance(Locale.US);
            String formattedNumber =numberformat.format(bank.getAccountBalance(accountNumber));
            modelAndView.addObject("balance", formattedNumber);
            modelAndView.addObject("accountNumber", accountNumber);
            return modelAndView;
        }else{
            ModelAndView modelAndView = new ModelAndView("InvalidWithdrawAmountPage");
            modelAndView.addObject("accountNumber", accountNumber);
            return modelAndView;
        }
    }

    @PostMapping("/ShowBalance")
    public ModelAndView ShowBalanceMethod(@RequestParam("accountNum") int accountNumber) {
        ModelAndView modelAndView = new ModelAndView("ShowBalancePage");
        Bank bank = new Bank();
        //balanceを小数点削除してカンマ追加（フォーマットを修正）
        NumberFormat numberformat=NumberFormat.getIntegerInstance(Locale.US);
        String formattedNumber =numberformat.format(bank.getAccountBalance(accountNumber));
        modelAndView.addObject("balance", formattedNumber);
        modelAndView.addObject("accountNumber",accountNumber);
        return modelAndView;
    }
}
