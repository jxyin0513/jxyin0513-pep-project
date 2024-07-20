package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message insertMessage(Message message){
        if(message.getMessage_text().length()>0 && message.getMessage_text().length()<255){
            return messageDAO.insertMessage(message);
        }
        return null;
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id){
        return messageDAO.getMessageById(id);
    }

    public void deleteMessageById(int id){
         messageDAO.deleteMessageById(id);
    }

    public Message updateMessageById(int id, String message_text){
        Message message = messageDAO.getMessageById(id);
        if(message_text.length()>0 && message_text.length()<255 && message!=null){
            messageDAO.updateMessageById(id, message_text);
            return messageDAO.getMessageById(id);
        }
        return null;
    }

    public List<Message> getAllMessagesByUser(int id){
        return messageDAO.getAllMessagesByUser(id);
    }
}
