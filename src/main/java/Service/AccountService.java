package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;

    // Contructors
    public AccountService() {
        accountDAO = new AccountDAO();
    }
    public AccountService(AccountDAO accDAO) {
        accountDAO = accDAO;
    }
    
    // Functions for the assignment
    public Account registerAccount(Account acc) {
        Account checkAcc = accountDAO.findAccount(acc.username);
        if (checkAcc == null) {
            return this.accountDAO.createAccount(acc);
        }
        return null;
    }
    
    public Account findAccount(Account acc) {
        Account checkAcc = accountDAO.findAccount(acc.username);
        if (checkAcc != null) {
            if (checkAcc.getPassword().equals(acc.getPassword())) {
                return checkAcc;
            }
        }
        return null;
    }

    public Boolean doesAccountExist(int account_id) {
        return accountDAO.doesAccountExist(account_id);
    }
}
