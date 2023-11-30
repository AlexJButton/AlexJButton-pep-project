package Controller;

import static org.mockito.Mockito.calls;

import java.util.ArrayList;
import java.util.List;

import org.h2.util.json.JSONObject;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    // The service files needed to interact with the database
    private AccountService accountService = new AccountService();
    private MessageService messageService = new MessageService();


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        // The resiter account endpoint
        app.post("/register", this::registerAccount);

        // The login account endpoint
        app.post("/login", this::loginAccount);

        // The create message endpoint
        app.post("/messages", this::createMessage);

        // The get all messages endpoint
        app.get("/messages", this::getMessages);

        // The get message by id endpoint
        app.get("/messages/{message_id}", this::getMessageByID);

        // This is the endpoint to delete a message
        app.delete("/messages/{message_id}", this::deleteMessageByID);

        // The endpoint to update a message
        app.patch("/messages/{message_id}", this::updateMessage);

        // The endpoint to get all messages by an account
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccout);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * This is the register account endpoint
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @return The account username, password, and id returned as JSON.
     */
    private void registerAccount(Context context) {
        Account bodyAccount = context.bodyAsClass(Account.class);
        String username = bodyAccount.getUsername();
        String password = bodyAccount.getPassword();

        Boolean usernameCheck = (username != null) && (!username.isBlank());
        Boolean passwordCheck = (password != null) && (password.length() > 4);
        if (usernameCheck && passwordCheck) {
            Account newAccount = new Account(username, password);
            Account possibleAccount = accountService.registerAccount(newAccount);

            if (possibleAccount != null) {
                context.status(200).json(possibleAccount);
                return;
            } 
        }

        context.status(400).json("");
    }

    /**
     * This is the login endpoint
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @return The account username, password, and id returned as JSON.
     */
    private void loginAccount(Context context) {
        Account bodyAccount = context.bodyAsClass(Account.class);
        String username = bodyAccount.getUsername();
        String password = bodyAccount.getPassword();

        if (username != null && password != null) {
            Account newAccount = new Account(username, password);
            Account possibleAccount = accountService.findAccount(newAccount);

            if (possibleAccount != null) {
                context.status(200).json(possibleAccount);
                return;
            }
        }

        context.status(401).json("");
    }

    /**
     * This is the create message endpoint
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @return The created message along with it's id.
     */
    private void createMessage(Context context) {
        Message bodyMessage = context.bodyAsClass(Message.class);
        int posted_by = bodyMessage.getPosted_by();
        String message_text = bodyMessage.getMessage_text();
        Long time_posted_epoch = bodyMessage.getTime_posted_epoch();

        Boolean posted_byCheck = accountService.doesAccountExist(posted_by);
        Boolean message_textCheck = (message_text != null) && (message_text.length() <= 255) && (!message_text.isBlank());

        if (posted_byCheck && message_textCheck && time_posted_epoch != null) {
            Message newMessage = new Message(posted_by, message_text, time_posted_epoch);
            Message possibleMessage = messageService.createMessage(newMessage);

            if (possibleMessage != null) {
                context.status(200).json(possibleMessage);
                return;
            }
        }

        context.status(400).json("");
    }

    /**
     * This is the endpoint to get all messages as a list
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @return The list of messages in the database.
     */
    private void getMessages(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.status(200).json(messages);
    }

    /**
     * This is the endpoint to get a message by it's id
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @return The requested message.
     */
    private void getMessageByID(Context context) {
        String messageIdString = context.pathParam("message_id");
        try {
            int message_id = Integer.parseInt(messageIdString);
            Message possibleMessage = messageService.getMessageById(message_id);
            if (possibleMessage != null) {
                context.status(200).json(possibleMessage);
                return;
            }
            context.status(200).json("");
        } catch (Exception e) {
            context.status(200).json("");
        }
    }

    /**
     * This is the endpoint to delete a message by it's id
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @return The requested message.
     */
    private void deleteMessageByID(Context context) {
        String messageIdString = context.pathParam("message_id");
        try {
            int message_id = Integer.parseInt(messageIdString);
            Message possibleMessage = messageService.getMessageById(message_id);
            if (possibleMessage != null) {
                Message deletedMessage = messageService.deletMessageById(message_id);
                context.status(200).json(deletedMessage);
                return;
            }
            context.status(200).json("");
        } catch (Exception e) {
            context.status(200).json("");
        }
    }

    /**
     * This is the endpoint used to update a message
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @return The message is returned if it exists, else it is blank.
     */
    private void updateMessage(Context context) {
        Message bodyMessage = context.bodyAsClass(Message.class);
        String message_id_string = context.pathParam("message_id");
        String message_text = bodyMessage.getMessage_text();

        try {
            int message_id = Integer.parseInt(message_id_string);
            Message possibleMessage = messageService.getMessageById(message_id);
            Boolean message_textCheck = (message_text == null) || (message_text.length() > 255) || (message_text.isBlank());
            if ((possibleMessage == null) || message_textCheck) {
                context.status(400).json("");
                return;
            }
            Message updatedMessage = messageService.updatMessageById(message_id, message_text);

            context.status(200).json(updatedMessage);
        } catch (Exception e) {
            context.status(400).json("");
        }
    }

    /**
     * This is the endpoint used to find the messages made by an account
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @return .
     */
    private void getMessagesByAccout(Context context) {
        String accountIdString = context.pathParam("account_id");
        List<Message> messages = new ArrayList<Message>();
        try {
            int account_id = Integer.parseInt(accountIdString);
            messages = messageService.getMessageByAccount(account_id);

            context.status(200).json(messages);
        } catch (Exception e) {
            context.status(200).json(messages);
        }
    }


}