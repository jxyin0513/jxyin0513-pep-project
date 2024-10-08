package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService accountService;
    MessageService messageService;
    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // app.start(8080);
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postUserLogInHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException, JsonMappingException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account newAccount = accountService.addUser(account);
        if(newAccount==null){
            ctx.status(400);
        }else{
            ctx.json(om.writeValueAsString(newAccount));
        }
    }
    private void postUserLogInHandler(Context ctx) throws JsonProcessingException, JsonMappingException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account login = accountService.userLogIn(account);
        if(login==null){
            ctx.status(401);
        }else{
            ctx.json(om.writeValueAsString(login));
        }
    }
    private void postMessageHandler(Context ctx) throws JsonProcessingException, JsonMappingException {
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        Message msg = messageService.insertMessage(message);
        if(msg==null){
            ctx.status(400);
        }else{
            ctx.json(msg);
        }
    }
    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }
    private void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if(message==null){
            ctx.status(200);
        }else{
            ctx.json(message);
        }
        
    }
    private void deleteMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if(message!=null){
            messageService.deleteMessageById(messageId);
            ctx.json(message);
        }else{
            ctx.status(200);
        }
    }
    private void patchMessageByIdHandler(Context ctx) throws JsonProcessingException, JsonMappingException {
        ObjectMapper om = new ObjectMapper();
        Message message_text = om.readValue(ctx.body(), Message.class);
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.updateMessageById(messageId, message_text.getMessage_text());
        System.out.println(message_text);
        if(message!=null){
            ctx.json(message);
        }else{
            ctx.status(400);
        }
    }
    private void getMessagesByUserHandler(Context ctx) {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByUser(account_id);
        ctx.json(messages);
    }
}