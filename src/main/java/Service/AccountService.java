package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addUser(Account account){
        Account checkAccount = accountDAO.getAccountById(account);
        if(account.getUsername().length()>0 && account.getPassword().length()>=4 && checkAccount==null){
            return accountDAO.insertAccount(account);
        }
        return null;
    }

    public Account userLogIn(Account account){
        Account checkAccount = accountDAO.getAccountById(account);
        if(checkAccount!= null && account.getPassword().equals(checkAccount.getPassword())){
            return checkAccount;
        }
        return null;
    }
}
