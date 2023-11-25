package Service;

import Model.Message;

import java.util.List;

import DAO.MessageDAO;

public class MessageService {
    public MessageDAO messageDAO;

    // Contructors
    public MessageService() {
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messDAO) {
        messageDAO = messDAO;
    }

    // Functions for the assignment
    public Message createMessage(Message message) {
        return this.messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return this.messageDAO.getAllMessages();
    }

    public Message getMessageById(int messID) {
        return this.messageDAO.getMessageById(messID);
    }

    public Message deletMessageById(int messID) {
        return this.messageDAO.deleteMessageById(messID);
    }

    public Message updatMessageById(int messID, String messText) {
        return this.messageDAO.updatMessageByID(messID, messText);
    }

    public List<Message> getMessageByAccount(int accID) {
        return this.messageDAO.getMessageByAccount(accID);
    }


    
}
