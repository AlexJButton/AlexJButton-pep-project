package Controller;

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
        app.post("messages", this::loginAccount);

        // The create message endpoint

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
        String username = context.formParam("username");
        String password = context.formParam("password");

        Boolean usernameCheck = (username != null) && (!username.isBlank());
        Boolean passwordCheck = (password != null) && (password.length() > 4);
        if (usernameCheck && passwordCheck) {
            Account newAccount = new Account(username, password);
            Account possibleAccount = accountService.registerAccount(newAccount);

            if (possibleAccount != null) {
                String jsonResponse = "{\"account_id\":" + possibleAccount.getAccount_id() +
                                       ",\"username\":\"" + possibleAccount.getUsername() +
                                       "\",\"password\":\"" + possibleAccount.getPassword() + "\"}";
                context.contentType("application/json").result(jsonResponse);
                context.status(200);
                return;
            } 
        }

        context.status(400).json("Given values were invalid or username already exists.");
    }

    /**
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @return The account username, password, and id returned as JSON.
     */
    private void loginAccount(Context context) {
        String username = context.formParam("username");
        String password = context.formParam("password");

        if (username != null && password != null) {
            Account newAccount = new Account(username, password);
            Account possibleAccount = accountService.findAccount(newAccount);

            if (possibleAccount != null) {
                String jsonResponse = "{\"account_id\":" + possibleAccount.getAccount_id() +
                                       ",\"username\":\"" + possibleAccount.getUsername() +
                                       "\",\"password\":\"" + possibleAccount.getPassword() + "\"}";
                context.contentType("application/json").result(jsonResponse);
                context.status(200);
                return;
            }
        }

        context.status(401).json("Username or password is incorrect");
    }

    /**
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @return .
     */
    private void createMessage(Context context) {
        String posted_by = context.formParam("posted_by");
        String message_text = context.formParam("message_text");
        String time_posted_epoch = context.formParam("time_posted_epoch");
        int posted_byInt = -1;
        long time_posted_epochLong = 0;

        try {
            posted_byInt = Integer.parseInt(posted_by);
            time_posted_epochLong = Long.parseLong(time_posted_epoch);
        } catch (Exception e){
            context.status(400).json("Invalid account ID or time posted epoch.");
        }
        Boolean posted_byCheck = (posted_by != null) && (accountService.doesAccountExist(posted_byInt));
        Boolean message_textCheck = (message_text != null) && (message_text.length() <= 255) && (!message_text.isBlank());

        if (posted_byCheck && message_textCheck && time_posted_epoch != null) {
            Message newMessage = new Message(posted_byInt, message_text, time_posted_epochLong);
            Message possibleMessage = messageService.createMessage(newMessage);

            if (possibleMessage != null) {
                String jsonResponse = "{" +
                                    "\"message_id\":" + possibleMessage.getMessage_id() + "," +
                                    "\"posted_by\":" + possibleMessage.getPosted_by() + "," +
                                    "\"message_text\":\"" + possibleMessage.getMessage_text() + "\"," +
                                    "\"time_posted_epoch\":" + possibleMessage.getTime_posted_epoch() +
                                    "}";
                context.contentType("application/json").result(jsonResponse);
                context.status(200);
                return;
            }
        }

        context.status(400).json("Failed to create the message");
    }

    /**
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @return .
     */
    private void (Context context) {
        
    }


}