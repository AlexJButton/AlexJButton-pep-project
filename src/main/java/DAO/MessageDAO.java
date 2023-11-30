package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    // Function to find all messages
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    // Function to get a message by its id
    public Message getMessageById(int messID) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Making the command
            String sql = "SELECT * FROM message WHERE message_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messID);

            // Running the command
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Function to get a message by a user
    public List<Message> getMessageByAccount(int accID) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            // Making the command
            String sql = "SELECT * FROM message WHERE posted_by=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accID);

            // Running the command
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    // Function to delete a message by its id
    public Message deleteMessageById(int messID) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Checking if the message exists before continuing
            Message checkMessage = getMessageById(messID);
            if (checkMessage == null) {
                return null;
            }

            // Making the command
            String sql = "DELETE FROM message WHERE message_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messID);

            // Running the command
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                return checkMessage;
            }
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Function to update a message's text with its id
    public Message updatMessageByID(int messID, String messText) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Checking if the message exists before continuing
            Message checkMessage = getMessageById(messID);
            if (checkMessage == null) {
                return null;
            }

            // Making the command
            String sql = "UPDATE message SET message_text=? WHERE message_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, messText);
            preparedStatement.setInt(2, messID);

            // Running the command
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                checkMessage.setMessage_text(messText);
                return checkMessage;
            }
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Function to create a message
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Making the command
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            // Running the command
            int result = preparedStatement.executeUpdate();

            // Getting the account_id that was made for the new account
            if (result > 0) {
                ResultSet newKey = preparedStatement.getGeneratedKeys();
                if (newKey.next()) {
                    int messID = newKey.getInt(1);
                    message.setMessage_id(messID);
                }
            }

            return message;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
