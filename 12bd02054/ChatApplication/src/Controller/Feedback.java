package Controller;

import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class Feedback {

	static Channel replies = new Channel("queue4");
	static Vector<Message> messages = new Vector<>();
	
	public static void updateMesList() throws UnsupportedEncodingException, MessageQueueException{
		replies.open();
		while(replies.peek()!=null){			
			messages.add(replies.receive());
		}
		replies.close();
		System.out.println("size"+messages.size());
		
		
	}
	
	public static Message findTheMes(String id) throws UnsupportedEncodingException, MessageQueueException{
		updateMesList();
		System.out.println("list was updated!");
		for(int i=0; i<messages.size(); i++){
			System.out.println(messages.get(i).getBodyAsString());
		}
		System.out.println("label= "+id);
		for(int i=0; i<messages.size(); i++){
			if(messages.get(i).getLabel().equals(id)){
				System.out.println("it was here...");
				return messages.get(i);
			}
		}
		return null;
	}
	
}
