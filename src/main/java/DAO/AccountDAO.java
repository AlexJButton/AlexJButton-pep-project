package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    // Function to find an account by its username
    public Account findAccount(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Making the command
            String sql = "SELECT * FROM account WHERE username=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            // Running the command
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account acc = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password"));
                return acc;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Funtion to add an account to the table
    public Account createAccount(Account acc) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Making the command
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, acc.getUsername());
            preparedStatement.setString(2, acc.getPassword());

            // Running the command
            int result = preparedStatement.executeUpdate();

            // Getting the account_id that was made for the new account
            if (result > 0) {
                ResultSet newKey = preparedStatement.getGeneratedKeys();
                if (newKey.next()) {
                    int accId = newKey.getInt(1);
                    acc.setAccount_id(accId);
                }
            }

            return acc;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Function to see if a user ID is in the table
    public Boolean doesAccountExist(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Making the command
            String sql = "SELECT * FROM account WHERE account_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);

            // Running the command
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return true;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
